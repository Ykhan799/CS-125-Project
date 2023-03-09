package com.example.cs125project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


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
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(currentUser)) {
                    readFromDatabase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(currentUser)) {
                        Log.d("User Exists", "Check")
                        updateDatabaseValues(getHeight.toInt(), getWeight.toInt(), getAge.toInt())
                    }
                    else {
                        writeToDatabase(getHeight.toInt(), getWeight.toInt(), getAge.toInt())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
            val aboutToHomePage = Intent(this@AboutYou, HomePage::class.java)
            startActivity(aboutToHomePage)
            finish()
        }

    }


    private fun updateDatabaseValues(getHeight: Int, getWeight: Int, getAge: Int) {
        writeToDatabase(getHeight, getWeight, getAge)
    }


    private fun writeToDatabase(getHeight: Int, getWeight: Int, getAge: Int) {
        // child names
        val heightChild = "Height"
        val weightChild = "Weight"
        val ageChild = "Age"
        val preferencesChild = "Preferences"
        val option1Child = "Option 1"
        val option2Child = "Option 2"
        val option3Child = "Option 3"
        val option4Child = "Option 4"

        // get remaining values
        val buildMuscle = buildMuscleBox.text.toString()
        val gainWeight = gainWeightBox.text.toString()
        val loseWeight = loseWeightBox.text.toString()
        val flexibility = increaseFlexibilityBox.text.toString()

        // add height, weight, and age to database with user being the main key
        databaseReference.child(currentUser).child(heightChild).setValue(getHeight)
        databaseReference.child(currentUser).child(weightChild).setValue(getWeight)
        databaseReference.child(currentUser).child(ageChild).setValue(getAge)

        // add check box values to database only if they are checked. If not checked, add empty string instead
        if (buildMuscleBox.isChecked) {
            databaseReference.child(currentUser).child(preferencesChild).child(option1Child).setValue(buildMuscle)
        }
        else {
            databaseReference.child(currentUser).child(preferencesChild).child(option1Child).setValue("")
        }
        if (gainWeightBox.isChecked) {
            databaseReference.child(currentUser).child(preferencesChild).child(option2Child).setValue(gainWeight)
        }
        else {
            databaseReference.child(currentUser).child(preferencesChild).child(option2Child).setValue("")
        }
        if (loseWeightBox.isChecked) {
            databaseReference.child(currentUser).child(preferencesChild).child(option3Child).setValue(loseWeight)
        }
        else {
            databaseReference.child(currentUser).child(preferencesChild).child(option3Child).setValue("")
        }
        if (increaseFlexibilityBox.isChecked) {
            databaseReference.child(currentUser).child(preferencesChild).child(option4Child).setValue(flexibility)
        }
        else {
            databaseReference.child(currentUser).child(preferencesChild).child(option4Child).setValue("")
        }
    }

    // TODO: When user exists, show their previous data is loaded in
    private fun readFromDatabase() {
        Toast.makeText(this, "Reading from Database", Toast.LENGTH_SHORT).show()
    }
}