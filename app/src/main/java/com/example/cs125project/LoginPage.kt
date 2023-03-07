package com.example.cs125project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginPage : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginUser: Button
    private lateinit var noAccount: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page);

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginUser = findViewById(R.id.loginButton)
        noAccount = findViewById(R.id.noAccountButton)

        auth = FirebaseAuth.getInstance()

        loginUser.setOnClickListener {
            val getEmail = email.text.toString()
            val getPassword = password.text.toString()
            loginUser(getEmail, getPassword)
        }

        noAccount.setOnClickListener {
            val loginToCreateAccount = Intent(this@LoginPage, CreateAccount::class.java)
            startActivity(loginToCreateAccount)
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "$email logged in successfully!", Toast.LENGTH_SHORT).show()

            // Transitions from LoginActivity to Main Activity
            val loginToMain = Intent(this@LoginPage, HomePage::class.java)
            startActivity(loginToMain)
            finish()
        }

        auth.signInWithEmailAndPassword(email, password).addOnFailureListener {
            Toast.makeText(this, "Invalid Credentials. Please try again!", Toast.LENGTH_SHORT).show()
        }
    }
}