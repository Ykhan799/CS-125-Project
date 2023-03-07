package com.example.cs125project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AboutYou : AppCompatActivity() {
    private lateinit var userHeight: EditText
    private lateinit var userWeight: EditText
    private lateinit var userAge: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_you)

        saveButton = findViewById(R.id.saveButton)
        userHeight = findViewById(R.id.height)
        userWeight = findViewById(R.id.weight)
        userAge = findViewById(R.id.age)

        saveButton.setOnClickListener {
            val getHeight = userHeight.text.toString()
            val getWeight = userWeight.text.toString()
            val getAge = userAge.text.toString()
            validationChecks(getHeight, getWeight, getAge)
        }
    }

    private fun validationChecks(getHeight: String, getWeight: String, getAge: String) {
        // Performs input checks when user enters data
        if (getHeight.isEmpty()) {
            Toast.makeText(this, "Empty height. Must enter a height in inches", Toast.LENGTH_SHORT).show()
        } else if (getWeight.isEmpty()) {
            Toast.makeText(this, "Empty weight. Must enter a weight in lbs", Toast.LENGTH_SHORT).show()
        } else if (getAge.isEmpty()) {
            Toast.makeText(this, "Empty age. Must enter a age in years", Toast.LENGTH_SHORT).show()
        } else if (getHeight.toInt() <= 0) {
            Toast.makeText(this, "Height must be a positive value", Toast.LENGTH_SHORT).show()
        } else if (getWeight.toInt() <= 0) {
            Toast.makeText(this, "Weight must be a positive value", Toast.LENGTH_SHORT).show()
        } else if (getAge.toInt() <= 0) {
            Toast.makeText(this, "Age must be a positive value", Toast.LENGTH_SHORT).show()
        } else {
            // TODO: add data to firebase
            val aboutToHomePage = Intent(this@AboutYou, HomePage::class.java)
            startActivity(aboutToHomePage)
            finish()
        }

    }
}