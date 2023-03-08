package com.example.cs125project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AboutYou : AppCompatActivity() {
    private lateinit var userHeight: EditText
    private lateinit var userWeight: EditText
    private lateinit var userAge: EditText
    private lateinit var saveButton: Button
    private lateinit var buildMuscleBox: CheckBox
    private lateinit var gainWeightBox: CheckBox
    private lateinit var loseWeightBox: CheckBox
    private lateinit var increaseFlexibilityBox: CheckBox
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAuth: FirebaseAuth
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_you)

        saveButton = findViewById(R.id.saveButton)
        userHeight = findViewById(R.id.height)
        userWeight = findViewById(R.id.weight)
        userAge = findViewById(R.id.age)
        buildMuscleBox = findViewById(R.id.buildingMuscle)
        gainWeightBox = findViewById(R.id.gainingWeight)
        loseWeightBox = findViewById(R.id.losingWeight)
        increaseFlexibilityBox = findViewById(R.id.increaseFlexible)
        databaseReference = FirebaseDatabase.getInstance().reference
        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser!!.email.toString().substringBefore("@")

        // TODO: Loads Previous user data
        if (checkUserExists(currentUser)) {
            readFromDatabase()
        }

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
        } else if (!buildMuscleBox.isChecked && !gainWeightBox.isChecked
            && !loseWeightBox.isChecked && !increaseFlexibilityBox.isChecked) {
            Toast.makeText(this, "Must select at least one wellness interest", Toast.LENGTH_SHORT).show()
        } else {
            if (checkUserExists(currentUser)) {
                updateDatabaseValues(getHeight, getWeight, getAge)
            }
            else {
                writeToDatabase(getHeight, getWeight, getAge)
            }
            Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
            val aboutToHomePage = Intent(this@AboutYou, HomePage::class.java)
            startActivity(aboutToHomePage)
            finish()
        }

    }

    // TODO: Update user data
    private fun updateDatabaseValues(getHeight: String, getWeight: String, getAge: String) {
        Toast.makeText(this, "DatabaseExists", Toast.LENGTH_SHORT).show()
    }

    // TODO: Write data to database
    private fun writeToDatabase(getHeight: String, getWeight: String, getAge: String) {
        Toast.makeText(this, "Database Does not Exist", Toast.LENGTH_SHORT).show()
    }

    // TODO: When user exists, show their previous data is loaded in
    private fun readFromDatabase() {

    }

    private fun checkUserExists(getUser: String): Boolean {
        var checkUser = false
        val checkUserReference = databaseReference.child(currentUser)

        // Return true if userExists, false if current user doesn't exist
        checkUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    checkUser = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                checkUser = false
            }
        })
        return checkUser
    }
}