package com.example.cs125project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {
    private lateinit var userAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var logoutButton: Button
    private lateinit var editInformationButton: Button
    private lateinit var continueButton: Button
    private lateinit var levelText: TextView
    private lateinit var greetingText: TextView
    private lateinit var avatar: ImageView
    private lateinit var determinateBar: ProgressBar
    private lateinit var currentUser: String
    private lateinit var userLevel: Level


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        logoutButton = findViewById(R.id.logoutButton)
        continueButton = findViewById(R.id.continueButton)
        editInformationButton = findViewById(R.id.editInfoButton)
        levelText = findViewById(R.id.levelText)
        greetingText = findViewById(R.id.greetingText)
        determinateBar = findViewById(R.id.determinateBar)
        avatar = findViewById(R.id.avatar)
        userLevel = Level()

        databaseReference = FirebaseDatabase.getInstance().reference
        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser!!.email.toString().substringBefore("@")

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

        continueButton.setOnClickListener {
            val homeToRecommendationScreen = Intent(this@HomePage, RecommendationScreen::class.java)
            startActivity(homeToRecommendationScreen)
            finish()
        }

        // Get data from database
        val userReference = databaseReference.child(currentUser)
        userReference.get().addOnSuccessListener {
            if (it.exists()) {
                val uLevel = it.child(Constants.levelChild).value

                if (uLevel is Level) {
                    userLevel = uLevel
                }
            } else {
                Log.d("Context", "User does not exist")
            }
        }

        Log.d("HomePage", userLevel.currentLevel.toString())

        // Update level textview
        greetingText.text = "Hi " + currentUser + "!"
        levelText.text = "Level " + userLevel.currentLevel.toString()

        // Update progress bar based on xp
        determinateBar.progress = userLevel.currentExperience

        // Update avatar sprite based on level
        if (userLevel.currentLevel > 5 && userLevel.currentLevel <= 10) {
            avatar.setImageResource(R.drawable.sprite_s2)
        } else if (userLevel.currentLevel > 10) {
            avatar.setImageResource(R.drawable.sprite_s3)
        }
    }
}