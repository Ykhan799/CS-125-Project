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
import com.example.cs125project.Level


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
    private val TAG = "AboutYou"
    private lateinit var userLevel: Level


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
        userLevel = Level()

        // gets user data if user already exists
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(currentUser)) {
                    readFromDatabase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, error.toString())
            }
        })

        // saves new data user enters
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
        } else if (getHeight.toIntOrNull() == null || getHeight.toInt() <= 0) {
            Toast.makeText(this, "Height must be a positive integer", Toast.LENGTH_SHORT).show()
        } else if (getWeight.toIntOrNull() == null || getWeight.toInt() <= 0) {
            Toast.makeText(this, "Weight must be a positive integer", Toast.LENGTH_SHORT).show()
        } else if (getAge.toIntOrNull() == null || getAge.toInt() <= 0) {
            Toast.makeText(this, "Age must be a positive integer", Toast.LENGTH_SHORT).show()
        } else if (getHeight.toInt() > 107) {
            Toast.makeText(this, "Height is too large. Must be smaller", Toast.LENGTH_SHORT).show()
        } else if (getHeight.toInt() < 21) {
            Toast.makeText(this, "Height is too small. Must be larger", Toast.LENGTH_SHORT).show()
        } else if (getWeight.toInt() > 1400) {
            Toast.makeText(this, "Weight is too large. Must be smaller", Toast.LENGTH_SHORT).show()
        } else if (getWeight.toInt() < 5) {
            Toast.makeText(this, "Height is too small, Must be larger", Toast.LENGTH_SHORT).show()
        } else if (getAge.toInt() > 122) {
            Toast.makeText(this, "Age is too large. Must be smaller", Toast.LENGTH_SHORT).show()
        } else if (getAge.toInt() < 14) {
            Toast.makeText(this, "Age is too small. Must be larger", Toast.LENGTH_SHORT).show()
        } else if (!buildMuscleBox.isChecked && !gainWeightBox.isChecked
            && !loseWeightBox.isChecked && !increaseFlexibilityBox.isChecked) {
            Toast.makeText(this, "Must select at least one wellness interest", Toast.LENGTH_SHORT).show()
        } else {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(currentUser)) {
                        updateDatabaseValues(getHeight.toInt(), getWeight.toInt(), getAge.toInt())
                    }
                    else {
                        writeToDatabase(getHeight.toInt(), getWeight.toInt(), getAge.toInt())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toString())
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
        // get checkbox values
        val buildMuscle = buildMuscleBox.text.toString()
        val gainWeight = gainWeightBox.text.toString()
        val loseWeight = loseWeightBox.text.toString()
        val flexibility = increaseFlexibilityBox.text.toString()

        // add height, weight, and age to database with user being the main key
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.heightChild).setValue(getHeight)
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.weightChild).setValue(getWeight)
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.ageChild).setValue(getAge)
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option1Child).setValue("")
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option2Child).setValue("")
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option3Child).setValue("")
        databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option4Child).setValue("")
        databaseReference.child(currentUser).child(Constants.levelChild).setValue(userLevel)


        // add check box values to database only if they are checked. If not checked, add empty string instead
        if (buildMuscleBox.isChecked) {
            databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option1Child).setValue(buildMuscle)
        }
        if (gainWeightBox.isChecked) {
            databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option2Child).setValue(gainWeight)
        }
        if (loseWeightBox.isChecked) {
            databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option3Child).setValue(loseWeight)
        }
        if (increaseFlexibilityBox.isChecked) {
            databaseReference.child(currentUser).child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option4Child).setValue(flexibility)
        }
    }

    private fun readFromDatabase() {
        val userReference = databaseReference.child(currentUser)
        userReference.get().addOnSuccessListener {
            if (it.exists()) {
                val height = it.child(Constants.surveyChild).child(Constants.heightChild).value
                val weight = it.child(Constants.surveyChild).child(Constants.weightChild).value
                val age = it.child(Constants.surveyChild).child(Constants.ageChild).value
                val option1 = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option1Child).value
                val option2 = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option2Child).value
                val option3 = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option3Child).value
                val option4 = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option4Child).value
                val level = it.child(Constants.levelChild).value

                // prevents level from being reset when user already exists
                if (level is Level) {userLevel = level}

                Log.d(TAG, userLevel.currentLevel.toString())
                Log.d(TAG, userLevel.currentExperience.toString())
                Log.d(TAG, userLevel.nextExperience.toString())

                // updates all the labels
                userWeight.setText(weight.toString())
                userHeight.setText(height.toString())
                userAge.setText(age.toString())
                buildMuscleBox.isChecked = option1.toString().isNotEmpty()
                gainWeightBox.isChecked = option2.toString().isNotEmpty()
                loseWeightBox.isChecked = option3.toString().isNotEmpty()
                increaseFlexibilityBox.isChecked = option4.toString().isNotEmpty()
            }
            else {
                Log.d(TAG, "User does not exist")
            }
        }.addOnFailureListener {
            Log.d(TAG, it.toString())
        }
    }
}