package com.example.islam_shanasi.home_tiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.islam_shanasi.Data_Classes.Scholar
import com.example.islam_shanasi.MainActivity
import com.example.islam_shanasi.R
import com.example.islam_shanasi.databinding.ActivityScholarTileBinding
import com.google.firebase.database.*

class ScholarTile : AppCompatActivity() {

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

    fun init(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://islam-shanasi-default-rtdb.firebaseio.com/")
        cities = resources.getStringArray(R.array.Cities)
        fiqh = resources.getStringArray(R.array.Fiqh)
    }

    fun work(){
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fiqh)

        scholarTileBinding.scholarTileSpinnerFiqh.adapter = adapter2
        scholarTileBinding.scholarTileSpinnerCity.adapter = adapter1

        listeners()
    }

    fun listeners(){
        var city = ""
        var fiqh = ""
        var arraylist = ArrayList<Scholar?>()

        scholarTileBinding.ScholarTileSaveBtn.setOnClickListener {

            if(scholarTileBinding.scholarTileSpinnerFiqh.selectedItem.equals("Not Selected")){
                Toast.makeText(this, "Select Fiqh !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fiqh = scholarTileBinding.scholarTileSpinnerFiqh.selectedItem.toString()
            if(!scholarTileBinding.scholarTileSpinnerCity.selectedItem.equals("Not Selected"))
                city = scholarTileBinding.scholarTileSpinnerCity.selectedItem.toString()
            databaseReference.child("users").addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild("scholars")){
                        val childrens = snapshot.child("scholars").children
                        for(childID: DataSnapshot in childrens){
                            if(!city.equals("Not Selected")){
                                if((childID.child("fiqh").value?.equals(fiqh) == true) &&
                                    (childID.child("city").value?.equals(city) == true)
                                ){
                                    arraylist.add(childID.getValue(Scholar::class.java))
                                    println("##### Line 67 ScholarTile.kt list = $arraylist")
                                }
                            }

                            if(childID.child("fiqh").value?.equals(fiqh) == true){
                                arraylist.add(childID.getValue(Scholar::class.java))
                                println("##### Line 67 ScholarTile.kt list = $arraylist")
                            }
                        }
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