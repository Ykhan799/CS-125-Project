package com.example.cs125project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_about_you.*
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_recommendations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.api-ninjas.com/"
class RecommendationScreen : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: String
    private lateinit var userAuth: FirebaseAuth

    private lateinit var userHeight: String
    private lateinit var userWeight: String
    private lateinit var userAge: String
    private var buildMuscle: Boolean = false
    private var gainWeight: Boolean = false
    private var loseWeight: Boolean = false
    private var improveFlexibility: Boolean = false
    private lateinit var userLevel: Level

    private var offset: Int = 0
    private var atGym: Boolean = true

    private lateinit var workout1Name: TextView
    private lateinit var workout1Instructions: TextView
    private lateinit var workout2Name: TextView
    private lateinit var workout2Instructions: TextView

    private lateinit var workout1Complete: Button
    private lateinit var workout2Complete: Button
    private lateinit var recommend: Button


    fun randomOffset(number: Int): Int {
        var num = 0
        do {
            num = (0..300).random()
        } while(num <= number + 10 && num >= number - 10)

        return num
    }

    fun setFrontEnd(exercises: MutableMap<String, String>){
        recommend.visibility = View.INVISIBLE
        recommend.isEnabled = false
        workout1Complete.visibility = View.VISIBLE
        workout1Complete.isEnabled = true
        workout2Complete.visibility = View.VISIBLE
        workout2Complete.isEnabled = true
        val workoutNames = exercises.keys
        val workoutOne = workoutNames.elementAt(0)
        val workoutTwo = workoutNames.elementAt(1)
        val workoutOneInstructions = exercises[workoutOne]
        val workoutTwoInstructions = exercises[workoutTwo]
        workout1Name.text = workoutOne
        workout2Name.text = workoutTwo
        workout1Instructions.text = workoutOneInstructions
        workout2Instructions.text = workoutTwoInstructions
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)
        databaseReference = FirebaseDatabase.getInstance().reference
        offset = 0

        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser!!.email.toString().substringBefore("@")

        workout1Instructions = findViewById(R.id.workoutOne)
        workout1Name = findViewById(R.id.workoutOneName)
        workout2Name = findViewById(R.id.workoutTwoName)
        workout2Instructions = findViewById(R.id.workoutTwo)
        workout1Complete = findViewById(R.id.workoutOneButton)
        workout2Complete = findViewById(R.id.workoutTwoButton)
        recommend = findViewById(R.id.recommendButton)



        //Get user Context
        val userReference = databaseReference.child(currentUser)
        userReference.get().addOnSuccessListener {
            if(it.exists()){
                val uHeight = it.child(Constants.surveyChild).child(Constants.heightChild).value
                val uWeight = it.child(Constants.surveyChild).child(Constants.weightChild).value
                val uAge = it.child(Constants.surveyChild).child(Constants.ageChild).value
                val bMuscle = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option1Child).value //Priority 1
                val gWeight = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option2Child).value //Priority 4
                val lWeight = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option3Child).value //Priority 3
                val iFlexibility = it.child(Constants.surveyChild).child(Constants.preferencesChild).child(Constants.option4Child).value //Priority 2
                val uLevel = it.child(Constants.levelChild).value

                userHeight = uHeight.toString()
                userWeight = uWeight.toString()
                userAge = uAge.toString()
                buildMuscle = bMuscle.toString().isNotEmpty()
                gainWeight = gWeight.toString().isNotEmpty()
                loseWeight = lWeight.toString().isNotEmpty()
                improveFlexibility = iFlexibility.toString().isNotEmpty()
                if(uLevel is Level){userLevel = uLevel}
            }else{
                Log.d("Context","User does not exist")
            }
        }.addOnFailureListener{
            Log.d("Context",it.toString())
        }
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        var workouts = mutableMapOf("key" to "value")
        workouts.remove("key")
        buildMuscle = true
        userLevel = Level()
        Log.d("Reponse", workouts.count().toString())
        if (buildMuscle) {
            //Do Something
            offset = randomOffset(offset)
            viewModel.getCustomPosts("strength", offset)
            viewModel.myCustomPosts.observe(this, Observer { response ->
                    response.body()?.forEach {
                        if (userLevel.currentLevel <= 5) {
                            //Beginner
                            Log.d("Response", "userLevel")
                            if (it.difficulty == "beginner") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        //workouts[it.name] = it.instructions
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    //workouts[it.name] = it.instructions
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else if (userLevel.currentLevel in 6..15) {
                            //Intermediate
                            if (it.difficulty == "intermediate") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "expert"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else {
                            //Advanced
                            if (it.difficulty == "expert") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        }
                    }
                //}else{
                    //Log.d("Response", response.code().toString())
                //}
            })
        } else if (improveFlexibility) {
            //Do Something
            viewModel.getCustomPosts("plyometrics", offset)
            offset = randomOffset(offset)
            viewModel.myCustomPosts.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        if (userLevel.currentLevel <= 5) {
                            //Beginner
                            if (it.difficulty == "beginner") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else if (userLevel.currentLevel in 6..15) {
                            //Intermediate
                            if (it.difficulty == "intermediate") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "expert"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else {
                            //Advanced
                            if (it.difficulty == "expert") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Log.d("Response", response.code().toString())
                }
            })
        } else if (loseWeight) {
            //Do Something
            viewModel.getCustomPosts("cardio", offset)
            offset = randomOffset(offset)
            viewModel.myCustomPosts.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        if (userLevel.currentLevel <= 5) {
                            //Beginner
                            if (it.difficulty == "beginner") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else if (userLevel.currentLevel in 6..15) {
                            //Intermediate
                            if (it.difficulty == "intermediate") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "expert"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else {
                            //Advanced
                            if (it.difficulty == "expert") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Log.d("Response", response.code().toString())
                }
            })
        } else if (gainWeight) {
            //Do Something
            viewModel.getCustomPosts("powerlifting", offset)
            offset = randomOffset(offset)
            viewModel.myCustomPosts.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        if (userLevel.currentLevel <= 5) {
                            //Beginner
                            if (it.difficulty == "beginner") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else if (userLevel.currentLevel in 6..15) {
                            //Intermediate
                            if (it.difficulty == "intermediate") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "expert"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        } else {
                            //Advanced
                            if (it.difficulty == "expert") {
                                //Add to Recommendation Screen
                                if(!atGym)
                                {
                                    if(it.equipment == "None")
                                    {
                                        workouts.put(it.name, it.instructions)
                                    }
                                }
                                else
                                {
                                    workouts.put(it.name, it.instructions)
                                }
                                if(workouts.count() < 2){
                                    response.body()?.forEach(){
                                        if (it.difficulty == "intermediate"){
                                            workouts.put(it.name, it.instructions)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Log.d("Response", response.code().toString())
                }
            })
        }

        workout1Complete.setOnClickListener{
            userLevel.gainExp(20)
            val recommendationToHomePage = android.content.Intent(
                this@RecommendationScreen,
                com.example.cs125project.HomePage::class.java
            )
            startActivity(recommendationToHomePage)
            finish()
        }

        workout2Complete.setOnClickListener{
            userLevel.gainExp(20)
            val recommendationToHomePage = android.content.Intent(
                this@RecommendationScreen,
                com.example.cs125project.HomePage::class.java
            )
            startActivity(recommendationToHomePage)
            finish()
        }

        recommend.setOnClickListener{
            setFrontEnd(workouts)
        }
    }
}

