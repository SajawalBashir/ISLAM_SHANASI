package com.example.islam_shanasi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.example.islam_shanasi.databinding.ActivitySignupBinding
import com.example.islam_shanasi.databinding.ProfilePicBinding
import com.example.islam_shanasi.databinding.ScholarCertificateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
    lateinit var certificateBinding: ScholarCertificateBinding
    lateinit var profilePicBinding: ProfilePicBinding
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var categories: Array<String>
    lateinit var genders: Array<String>
    lateinit var cities: Array<String>
    lateinit var countries: Array<String>
    lateinit var fiqh: Array<String>
    var scholCertPic: Intent? = null
    var profilePic: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        certificateBinding = ScholarCertificateBinding.inflate(layoutInflater)
        profilePicBinding = ProfilePicBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)

        init()
        work()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://islam-shanasi-default-rtdb.firebaseio.com/")
        storageReference = FirebaseStorage.getInstance()
            .getReferenceFromUrl("gs://islam-shanasi.appspot.com")
        categories = resources.getStringArray(R.array.categories)
        genders = resources.getStringArray(R.array.Gender)
        cities = resources.getStringArray(R.array.Cities)
        countries = resources.getStringArray(R.array.Countries)
        fiqh = resources.getStringArray(R.array.Fiqh)
    }

    private fun work() {

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        val adapter4 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
        val adapter5 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fiqh)

        signupBinding.spinnerCategory.adapter = adapter1
        signupBinding.spinnerGender.adapter = adapter2
        signupBinding.spinnerCity.adapter = adapter3
        signupBinding.spinnerCountry.adapter = adapter4
        signupBinding.spinnerFiqh.adapter = adapter5

        listeners()
    }

    fun listeners() {
        var name = ""
        var email = ""
        var mobile = ""
        var password = ""
        var category = ""
        var gender = ""
        var city = ""
        var country = ""
        var fiqh = ""

        signupBinding.imgBackSignup.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signupBinding.activitySignupLayoutNextImg.setOnClickListener {

            if (Common.fieldsRequired(

                    signupBinding.editTextFirstNameSignUpChild,
                    signupBinding.editTextLastNameSignUpChild,
                    signupBinding.editTextEmailSignupChild,
                    signupBinding.editTextMobileSignupChild,
                    signupBinding.editTextPasswordSignupChild
                )
            ) {
                return@setOnClickListener
            }

            name =
                signupBinding.editTextFirstNameSignUpChild.text.toString() + " " + signupBinding.editTextLastNameSignUpChild.text.toString()
            email = signupBinding.editTextEmailSignupChild.text.toString()
            mobile = signupBinding.editTextMobileSignupChild.text.toString()
            password = signupBinding.editTextPasswordSignupChild.text.toString()

            if (containsNumbers(signupBinding.editTextFirstNameSignUpChild.text.toString())) {
                if (containsNumbers(signupBinding.editTextLastNameSignUpChild.text.toString())) {
                    signupBinding.editTextLastNameSignUpChild.error = "Contains Numbers"
                    signupBinding.editTextFirstNameSignUpChild.error = "Contains Numbers"
                    return@setOnClickListener
                } else {
                    signupBinding.editTextFirstNameSignUpChild.error = "Contains Numbers"
                    return@setOnClickListener
                }
            }

//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                signupBinding.editTextEmailSignupChild.requestFocus()
//                signupBinding.editTextEmailSignupChild.error = "Invalid Email Format"
//                return@setOnClickListener
//            }

            if (signupBinding.spinnerCategory.selectedItem.equals("Not Selected") ||
                signupBinding.spinnerGender.selectedItem.equals("Not Selected") ||
                signupBinding.spinnerCity.selectedItem.equals("Not Selected") ||
                signupBinding.spinnerCountry.selectedItem.equals("Not Selected") ||
                signupBinding.spinnerFiqh.selectedItem.equals("Not Selected")
            ) {
                Toast.makeText(this, "Select All Dropdowns", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                category = signupBinding.spinnerCategory.selectedItem.toString()
                gender = signupBinding.spinnerGender.selectedItem.toString()
                city = signupBinding.spinnerCity.selectedItem.toString()
                country = signupBinding.spinnerCountry.selectedItem.toString()
                fiqh = signupBinding.spinnerFiqh.selectedItem.toString()

                if (category.equals("Scholar"))
                    setContentView(certificateBinding.root).toString()
                else
                    setContentView(profilePicBinding.root).toString()

            }

            certificateBinding.scholarImageLayoutUploadBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                                OR
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "images/*"
                startActivityForResult(Intent.createChooser(intent, "Select with ..."), 12)
//            val activityLaunch = registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult()
//            ) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    dataFromResult = result.data
//                    certificateBinding.scholarImageLayoutImg.setImageURI(dataFromResult?.data)
//                }
//            }
//            activityLaunch.launch(Intent.createChooser(intent, "Select with ..."))
            }


            certificateBinding.scholarImageLayoutFinishImg.setOnClickListener {
                if (scholCertPic == null) {
                    Toast.makeText(this, "Kindly Upload First !", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Toast.makeText(
                    this,
                    "dataFromResult is ${scholCertPic!!.data}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                setContentView(profilePicBinding.root)
            }

            certificateBinding.imgBackScholarCertificate.setOnClickListener {
                setContentView(signupBinding.root)
            }


            profilePicBinding.profilePicTvSkip.setOnClickListener {
                if (category.equals("Scholar")) {
                    if (saveData(
                            name = name,
                            email = email,
                            mobile = mobile,
                            password = password,
                            category = category,
                            gender = gender,
                            city = city,
                            country = country,
                            fiqh = fiqh,
                            scholCertPic = scholCertPic
                        )
                    ) {
                        Toast.makeText(
                            this,
                            "Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else
                    if (saveData(
                            name = name,
                            email = email,
                            mobile = mobile,
                            password = password,
                            category = category,
                            gender = gender,
                            city = city,
                            country = country,
                            fiqh = fiqh,
                        )
                    ) {
                        Toast.makeText(
                            this,
                            "Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

            }

            profilePicBinding.profilePicBackImg.setOnClickListener {
                if (category.equals("Scholar"))
                    setContentView(certificateBinding.root)
                else {
                    setContentView(signupBinding.root)
                }
            }

            profilePicBinding.profilePicLayoutUploadBtn.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK)
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                                OR
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "images/*"
                startActivityForResult(Intent.createChooser(intent, "Select with ..."), 14)
//            val activityLaunch = registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult()
//            ) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    dataFromResult = result.data
//                    certificateBinding.scholarImageLayoutImg.setImageURI(dataFromResult?.data)
//                }
//            }
//            activityLaunch.launch(Intent.createChooser(intent, "Select with ..."))
            }

            profilePicBinding.profilePicLayoutNextBtn.setOnClickListener {
                if (profilePic == null) {
                    Toast.makeText(this, "Kindly Upload First !", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Toast.makeText(
                    this,
                    "dataFromResult is ${profilePic!!.data}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                if (category.equals("Scholar")) {
                    if (saveData(
                            name,
                            email,
                            mobile,
                            password,
                            category,
                            gender,
                            city,
                            country,
                            fiqh,
                            scholCertPic,
                            profilePic
                        )
                    ) {
                        Toast.makeText(
                            this,
                            "Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else
                    if (saveData(
                            name = name,
                            email = email,
                            mobile = mobile,
                            password = password,
                            category = category,
                            gender = gender,
                            city = city,
                            country = country,
                            fiqh = fiqh,
                            profilePic = profilePic
                        )
                    ) {
                        Toast.makeText(
                            this,
                            "Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }


        }
    }

    private fun saveData(
        name: String,
        email: String,
        mobile: String,
        password: String,
        category: String,
        gender: String,
        city: String,
        country: String,
        fiqh: String,
        scholCertPic: Intent? = null,
        profilePic: Intent? = null
    ): Boolean {

        try {
//        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener {
//            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
//        }

            if (category.equals("Scholar")) {
                var scholCertPicRef = ""
                var profilePicRef = "SKIPPED"
                storageReference.child("images").child("certificates")
                    .child(UUID.randomUUID().toString()).putFile(
                        scholCertPic?.data as Uri
                    ).addOnSuccessListener {
                        scholCertPicRef = it.storage.path
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                if (profilePic != null) {
                    storageReference.child("images").child("profiles")
                        .child(UUID.randomUUID().toString())
                        .putFile(
                            profilePic.data as Uri
                        ).addOnSuccessListener {
                            profilePicRef = it.storage.path
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                databaseReference.child("users").child("scholars").child(email).child("name")
                    .setValue(name)
                databaseReference.child("users").child("scholars").child(email).child("mobile")
                    .setValue(mobile)
                databaseReference.child("users").child("scholars").child(email).child("password")
                    .setValue(password)
                databaseReference.child("users").child("scholars").child(email).child("gender")
                    .setValue(gender)
                databaseReference.child("users").child("scholars").child(email).child("city")
                    .setValue(city)
                databaseReference.child("users").child("scholars").child(email).child("country")
                    .setValue(country)
                databaseReference.child("users").child("scholars").child(email).child("fiqh")
                    .setValue(fiqh)
                databaseReference.child("users").child("scholars").child(email)
                    .child("scholCertPicRef")
                    .setValue(scholCertPicRef)
                databaseReference.child("users").child("scholars").child(email)
                    .child("profilePicRef")
                    .setValue(profilePicRef)
            } else {
                var profilePicRef = "SKIPPED"
                if (profilePic != null) {
                    storageReference.child("images").child("profiles")
                        .child(UUID.randomUUID().toString())
                        .putFile(
                            profilePic?.data as Uri
                        ).addOnSuccessListener {
                            profilePicRef = it.storage.path
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                databaseReference.child("users").child("non-scholars").child(email).child("name")
                    .setValue(name)
                databaseReference.child("users").child("non-scholars").child(email).child("mobile")
                    .setValue(mobile)
                databaseReference.child("users").child("non-scholars").child(email)
                    .child("password")
                    .setValue(password)
                databaseReference.child("users").child("non-scholars").child(email).child("gender")
                    .setValue(gender)
                databaseReference.child("users").child("non-scholars").child(email).child("city")
                    .setValue(city)
                databaseReference.child("users").child("non-scholars").child(email).child("country")
                    .setValue(country)
                databaseReference.child("users").child("non-scholars").child(email).child("fiqh")
                    .setValue(fiqh)
                databaseReference.child("users").child("non-scholars").child(email)
                    .child("profilePicRef")
                    .setValue(profilePicRef)
            }
            return true
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                scholCertPic = data
                certificateBinding.scholarImageLayoutImg.setImageURI(scholCertPic?.data)
            }
            if (requestCode == 14) {
                profilePic = data
                profilePicBinding.profilePicLayoutImg.setImageURI(profilePic?.data)
            }
        }
    }

    fun containsNumbers(inputString: String): Boolean {
        val regex = Regex("\\d+")
        return regex.containsMatchIn(inputString)
    }


//    private fun spinnerListeners(): Int {
//        var a = 0
//        signupBinding.spinnerCategory.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                //var pos = 0
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    Toast.makeText(
//                        this@SignupActivity,
//                        "Item Selected: ${categories.get(position)}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    map = map.plus(Pair("GENDER", genders.get(position)))
//                    a = 70
//                    //pos = position
//                    println("##### Line 101 $map")
//                }
//
////                fun changeValue(): Int{
////                    map = map.plus(Pair("GENDER", genders.get(pos)))
////                    a = 70
////                    return a
////                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//            }
//
//        signupBinding.spinnerGender.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
////                var pos = 0
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    Toast.makeText(
//                        this@SignupActivity,
//                        "Item Selected: ${genders.get(position)}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    map = map.plus(Pair("GENDER", genders.get(position)))
//                    a = 70
////                    pos = position
//                    println("##### Line 125 $map")
//                }
//
////                fun changeValue(): Int{
////                    map = map.plus(Pair("GENDER", genders.get(pos)))
////                    a = 70
////                    return a
////                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//
//
//            }
//
//        signupBinding.spinnerCity.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    Toast.makeText(
//                        this@SignupActivity,
//                        "Item Selected: ${cities.get(position)}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    map = map.plus(Pair("CITY", cities.get(position)))
//                    a = 70
//                    println("##### Line 149 $map")
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//
//
//            }
//
//        signupBinding.spinnerCountry.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    Toast.makeText(
//                        this@SignupActivity,
//                        "Item Selected: ${countries.get(position)}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    map = map.plus(Pair("COUNTRY", countries.get(position)))
//                    a = 70
//                    println("##### Line 173 $map")
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//            }
//        return a
//    }
}