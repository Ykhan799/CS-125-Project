package com.example.cs125project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class CreateAccount : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var makeAccount: Button
    private lateinit var existingAccount: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        makeAccount = findViewById(R.id.createAccountButton)
        existingAccount = findViewById(R.id.alreadyCreatedButton)

        auth = FirebaseAuth.getInstance()

        makeAccount.setOnClickListener {
            val getEmail = email.text.toString()
            val getPassword = password.text.toString()

            if (getEmail.isEmpty()) {
                Toast.makeText(this, "Empty username. Must enter a username", Toast.LENGTH_SHORT).show()
            }
            else if (getPassword.isEmpty()) {
                Toast.makeText(this, "Empty password. Must enter a password", Toast.LENGTH_SHORT).show()
            }
            else {
                registerUser(getEmail, getPassword)
            }
        }

        existingAccount.setOnClickListener {
            val createToLogin = Intent(this@CreateAccount, LoginPage::class.java)
            startActivity(createToLogin)
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registering $email successful!", Toast.LENGTH_SHORT)
                    .show()

                val registerToMain = Intent(this@CreateAccount, AboutYou::class.java)
                startActivity(registerToMain)
                finish()
            } else {
                Toast.makeText(this, "Registration failed! Please try again!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}