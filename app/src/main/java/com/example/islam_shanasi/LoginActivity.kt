package com.example.islam_shanasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.islam_shanasi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var activityLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)
        init()
        work()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
    }

    private fun work() {
        activityLoginBinding.buttonLogin.setOnClickListener {
            val email = activityLoginBinding.editTextEmailLoginChild.text
            val password = activityLoginBinding.editTextPasswordLoginChild.text
            if (Common.fieldsRequired(
                    activityLoginBinding.editTextEmailLoginChild,
                    activityLoginBinding.editTextPasswordLoginChild
                )
            ) {
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
        activityLoginBinding.textDontHaveAccount.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}