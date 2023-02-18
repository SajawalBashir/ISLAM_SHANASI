package com.example.islam_shanasi

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.example.islam_shanasi.databinding.ActivitySignupBinding
import com.example.islam_shanasi.databinding.ProfilePicBinding
import com.example.islam_shanasi.databinding.ScholarCertificateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.net.URI
import java.security.SecureRandom
import java.util.UUID
import kotlin.concurrent.thread

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
    var id = ""

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

            if (containsNumbers(signupBinding.editTextFirstNameSignUpChild.text.toString()) ||
                containsNumbers(signupBinding.editTextLastNameSignUpChild.text.toString())
            ) {
                Toast.makeText(this, "Name should not contain Numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupBinding.editTextEmailSignupChild.requestFocus()
                signupBinding.editTextEmailSignupChild.error = "Invalid Email Format"
                return@setOnClickListener
            }

            if (password.length < 6) {
                signupBinding.editTextPasswordSignupChild.requestFocus()
                signupBinding.editTextPasswordSignupChild.error =
                    "Password should be at least 6 characters"
                return@setOnClickListener
            }



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
        }

        certificateBinding.scholarImageLayoutUploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                                OR
//            val intent = Intent(Intent.ACTION_PICK)
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
                        "Account Created Kindly Login Now",
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
                        "Account Created Kindly Login Now",
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
//            val intent = Intent(Intent.ACTION_PICK)
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
                        "Account Created Kindly Login Now",
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
                        "Account Created Kindly Login Now",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Auth Success", Toast.LENGTH_SHORT).show()
                    id = auth.currentUser?.uid.toString()
                    println("##### Line 361 SignupActivity.kt uid: $id")

                    if (category.equals("Scholar")) {
                        var scholCertPicRef = "N/A"
                        var profilePicRef = "N/A"

//                        val id = databaseReference.push().key.toString()

                        println("##### Line 369 SignupActivity.kt id= $id")

                        databaseReference.child("users").child("scholars").child(id).child("name")
                            .setValue(name)
                        databaseReference.child("users").child("scholars").child(id).child("email")
                            .setValue(email)
                        databaseReference.child("users").child("scholars").child(id).child("mobile")
                            .setValue(mobile)
                        databaseReference.child("users").child("scholars").child(id)
                            .child("password")
                            .setValue(password)
                        databaseReference.child("users").child("scholars").child(id).child("gender")
                            .setValue(gender)
                        databaseReference.child("users").child("scholars").child(id).child("city")
                            .setValue(city)
                        databaseReference.child("users").child("scholars").child(id)
                            .child("country")
                            .setValue(country)
                        databaseReference.child("users").child("scholars").child(id).child("fiqh")
                            .setValue(fiqh)
                        databaseReference.child("users").child("scholars").child(id).child("rating")
                            .setValue("0")
                        databaseReference.child("users").child("scholars").child(id)
                            .child("verified")
                            .setValue("0")
                        databaseReference.child("users").child("scholars").child(id)
                            .child("scholCertPicRef")
                            .setValue(scholCertPicRef)
                        databaseReference.child("users").child("scholars").child(id)
                            .child("profilePicRef")
                            .setValue(profilePicRef)



                    } else {
                        var profilePicRef = "N/A"

//                        val id = databaseReference.push().key.toString()

                        println("##### Line 408 SignupActivity.kt id= $id")

                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("name")
                            .setValue(name)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("mobile")
                            .setValue(mobile)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("password")
                            .setValue(password)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("gender")
                            .setValue(gender)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("city")
                            .setValue(city)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("country")
                            .setValue(country)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("fiqh")
                            .setValue(fiqh)
                        databaseReference.child("users").child("non-scholars").child(id)
                            .child("profilePicRef")
                            .setValue(profilePicRef)



                    }
                }.addOnFailureListener {
                    println("##### Line 364 SignupActivity.kt Auth Error: ${it.message}")
                }

            if (category.equals("Scholar")) {
                var scholCertPicRef = "N/A"
                var profilePicRef = "N/A"

                val list = ('a'..'b') + ('0'..'9') + ('A'..'Z')
                val random = SecureRandom()
                var key = (1..30).map { random.nextInt(list.size) }.map(list::get).joinToString("")

                storageReference.child("images").child("certificates")
//                            .child(UUID.randomUUID().toString())
                    .child(key)
                    .putFile(
                        scholCertPic?.data as Uri
                    ).addOnSuccessListener {
                        scholCertPicRef = it.storage.path
                        databaseReference.child("users").child("scholars").child(id)
                            .child("scholCertPicRef")
                            .setValue(scholCertPicRef)
                        println("##### Line 512 SignupActivity.kt id= $id")
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Line 388 SignupActivity.kt Error: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                if (profilePic != null) {

                    key = (1..30).map { random.nextInt(list.size) }.map(list::get).joinToString("")

                    storageReference.child("images").child("profiles")
//                                .child(UUID.randomUUID().toString())
                        .child(key)
                        .putFile(
                            profilePic.data as Uri
                        ).addOnSuccessListener {
                            profilePicRef = it.storage.path
                            databaseReference.child("users").child("scholars").child(id)
                                .child("profilePicRef")
                                .setValue(profilePicRef)
                            println("##### Line 534 SignupActivity.kt id= $id")
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Line 400 SignupActivity.kt Error: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            } else {
                var profilePicRef = "N/A"

                val list = ('a'..'b') + ('0'..'9') + ('A'..'Z')
                val random = SecureRandom()
                val key = (1..30).map { random.nextInt(list.size) }.map(list::get).joinToString("")

                if (profilePic != null) {
                    storageReference.child("images").child("profiles")
//                                .child(UUID.randomUUID().toString())
                        .child(key)
                        .putFile(
                            profilePic.data as Uri
                        ).addOnSuccessListener {
                            profilePicRef = it.storage.path
                            databaseReference.child("users").child("non-scholars").child(id)
                                .child("profilePicRef")
                                .setValue(profilePicRef)
                            println("##### Line 562 SignupActivity.kt id= $id")
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Line 434 SignupActivity.kt Error: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            return true
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Line 440 SignupActivity.kt Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                scholCertPic = data

//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,scholCertPic?.data)
//                println("##### Line 464 SignupActivity.kt bitmap.byteCount = ${bitmap.si}")


//                val path = Environment.getExternalStorageDirectory() + "$scholCertPic?.data"
//                val file: File = File(scholCertPic?.data?.path.toString())
//                val length = file.length()
//                println("##### Line 472 SignupActivity.kt scholCertPic?.data.path = ${scholCertPic?.data?.path}")
                val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
                val cursor = scholCertPic?.data?.let {
                    contentResolver.query(
                        it,
                        projection,
                        null,
                        null,
                        null
                    )
                }
                if (cursor != null) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val filePath = cursor.getString(columnIndex)
                    println("##### Line 483 SignupActivity.kt filePath = $filePath")
                    println("##### Line 484 SignupActivity.kt columnIndex = $columnIndex")
                    println("##### Line 484 SignupActivity.kt cursor = $cursor")
                    println("##### Line 484 SignupActivity.kt projection = $projection")

                    val file = File(filePath)
                    println("##### Line 484 SignupActivity.kt file.length() = ${file.length()}")
                    if (file.length() <= 500000) {
                        certificateBinding.scholarImageLayoutImg.setImageURI(scholCertPic?.data)
                    } else {
                        Toast.makeText(this, "File should be less than 500kb", Toast.LENGTH_SHORT)
                            .show()

                        scholCertPic = null

                    }
                }

            }
            if (requestCode == 14) {
                profilePic = data

//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,profilePic?.data)
//                println("##### Line 472 SignupActivity.kt bitmap.byteCount = ${bitmap.byteCount}")
                println("##### Line 481 SignupActivity.kt profilePic?.data.path = ${profilePic?.data?.path}")

                val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
                val cursor = profilePic?.data?.let {
                    contentResolver.query(
                        it,
                        projection,
                        null,
                        null,
                        null
                    )
                }
                if (cursor != null) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val filePath = cursor.getString(columnIndex)
                    println("##### Line 483 SignupActivity.kt filePath = $filePath")
                    println("##### Line 484 SignupActivity.kt columnIndex = $columnIndex")
                    println("##### Line 484 SignupActivity.kt cursor = $cursor")
                    println("##### Line 484 SignupActivity.kt projection = $projection")

                    val file = File(filePath)
                    println("##### Line 484 SignupActivity.kt file.length() = ${file.length()}")
                    if (file.length() <= 500000) {
                        profilePicBinding.profilePicLayoutImg.setImageURI(profilePic?.data)
                    } else {
                        Toast.makeText(this, "File should be less than 500kb", Toast.LENGTH_SHORT)
                            .show()
                        profilePic = null

                    }
                }

            }
        }
    }

    fun containsNumbers(inputString: String): Boolean {
        val regex = Regex("\\d+")
        return regex.containsMatchIn(inputString)
    }
}