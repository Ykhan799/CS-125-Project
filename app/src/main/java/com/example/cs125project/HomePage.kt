package com.example.cs125project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class HomePage : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var editInformationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        logoutButton = findViewById(R.id.logoutButton)
        editInformationButton = findViewById(R.id.editInfoButton)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_SHORT).show()

            val mainToStart = Intent(this@HomePage, CreateAccount::class.java)
            startActivity(mainToStart)
            finish()
        }

        editInformationButton.setOnClickListener {
            val homeToAboutYouPage = Intent(this@HomePage, AboutYou::class.java)
            startActivity(homeToAboutYouPage)
            finish()
        }
    }
}