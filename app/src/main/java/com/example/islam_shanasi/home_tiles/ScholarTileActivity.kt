package com.example.islam_shanasi.home_tiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.islam_shanasi.Data_Classes.Scholar
import com.example.islam_shanasi.MainActivity
import com.example.islam_shanasi.R
import com.example.islam_shanasi.custom_adapters.scholarTileCustomAdapter
import com.example.islam_shanasi.databinding.ActivityScholarTileBinding
import com.google.firebase.database.*

class ScholarTileActivity : AppCompatActivity() {

    lateinit var scholarTileBinding: ActivityScholarTileBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var cities: Array<String>
    lateinit var fiqh: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scholarTileBinding = ActivityScholarTileBinding.inflate(layoutInflater)
        setContentView(scholarTileBinding.root)

        init()
        work()
    }

    fun init() {
        databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://islam-shanasi-default-rtdb.firebaseio.com/")
        cities = resources.getStringArray(R.array.Cities)
        fiqh = resources.getStringArray(R.array.Fiqh)
    }

    fun work() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Scholars"
        }

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fiqh)

        scholarTileBinding.scholarTileSpinnerFiqh.adapter = adapter2
        scholarTileBinding.scholarTileSpinnerCity.adapter = adapter1

        listeners()
    }

    fun listeners() {
        var city = ""
        var fiqh = ""

        scholarTileBinding.ScholarTileSaveBtn.setOnClickListener {
            val arraylist = ArrayList<Scholar>()

            if (scholarTileBinding.scholarTileSpinnerFiqh.selectedItem.equals("Not Selected")) {
                Toast.makeText(this, "Select Fiqh !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fiqh = scholarTileBinding.scholarTileSpinnerFiqh.selectedItem.toString()
            city = scholarTileBinding.scholarTileSpinnerCity.selectedItem.toString()
            databaseReference.child("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("scholars")) {
                        val childrens = snapshot.child("scholars").children
                        for (childID: DataSnapshot in childrens) {
//                            println("##### Line 68 ScholarTileActivity.kt city = $city")
                            if (city != "Not Selected") {
//                                println("##### Line 70 ScholarTileActivity.kt list = $arraylist")
                                if ((childID.child("fiqh").value?.equals(fiqh) == true) &&
                                    (childID.child("city").value?.equals(city) == true)
                                ) {
                                    arraylist.add(childID.getValue(Scholar::class.java) as Scholar)
//                                    println("##### Line 74 ScholarTileActivity.kt list = $arraylist")
                                }
                            } else {
                                if (childID.child("fiqh").value?.equals(fiqh) == true) {
                                    arraylist.add(childID.getValue(Scholar::class.java) as Scholar)
//                                    println("##### Line 79 ScholarTileActivity.kt list = $arraylist")
                                }
                            }
                        }
                        scholarTileBinding.scholarTileRecylerView.layoutManager =
                            LinearLayoutManager(this@ScholarTileActivity)
                        scholarTileBinding.scholarTileRecylerView.adapter =
                            scholarTileCustomAdapter(this@ScholarTileActivity, arraylist)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}