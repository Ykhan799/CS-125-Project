package com.example.cs125project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_about_you.*
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

    fun randomOffset(number: Int): Int {
        var num = 0
        do {
            num = (0..1000).random()
        } while(num <= number + 10 || num >= number - 10)

        return num

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser!!.email.toString().substringBefore("@")


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

        viewModel.getPost()
        viewModel.myReponse.observe(this, Observer { response ->
            if(response.isSuccessful){
                response.body()?.forEach{
                    Log.d("Response", it.name)
                    Log.d("Response","----------------------")
                }
            }else{
                Log.d("Response", response.code().toString())
            }
        })

        if(buildMuscle)
        {
            //Do Something
            viewModel.getCustomPosts("strength", offset)
            offset = randomOffset(offset)
            viewModel.myReponse.observe(this, Observer{ response ->
                if(response.isSuccessful){
                    response.body()?.forEach{
                        if(userLevel.currentLevel <= 5)
                        {
                            //Beginner
                            if(it.difficulty == "beginner")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else if(userLevel.currentLevel > 5 && userLevel.currentLevel <= 15)
                        {
                            //Intermediate
                            if(it.difficulty == "intermediate")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else
                        {
                            //Advanced
                            if(it.difficulty == "expert")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                    }
                }
            })
        }
        else if(improveFlexibility)
        {
            //Do Something
            viewModel.getCustomPosts("plyometrics", offset)
            offset = randomOffset(offset)
            viewModel.myReponse.observe(this, Observer{ response ->
                if(response.isSuccessful){
                    response.body()?.forEach{
                        if(userLevel.currentLevel <= 5)
                        {
                            //Beginner
                            if(it.difficulty == "beginner")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else if(userLevel.currentLevel > 5 && userLevel.currentLevel <= 15)
                        {
                            //Intermediate
                            if(it.difficulty == "intermediate")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else
                        {
                            //Advanced
                            if(it.difficulty == "expert")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                    }
                }
            })
        }
        else if(loseWeight)
        {
            //Do Something
            viewModel.getCustomPosts("cardio", offset)
            offset = randomOffset(offset)
            viewModel.myReponse.observe(this, Observer{ response ->
                if(response.isSuccessful){
                    response.body()?.forEach{
                        if(userLevel.currentLevel <= 5)
                        {
                            //Beginner
                            if(it.difficulty == "beginner")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else if(userLevel.currentLevel > 5 && userLevel.currentLevel <= 15)
                        {
                            //Intermediate
                            if(it.difficulty == "intermediate")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else
                        {
                            //Advanced
                            if(it.difficulty == "expert")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                    }
                }
            })
        }
        else if(gainWeight)
        {
            //Do Something
            viewModel.getCustomPosts("powerlifting", offset)
            offset = randomOffset(offset)
            viewModel.myReponse.observe(this, Observer{ response ->
                if(response.isSuccessful){
                    response.body()?.forEach{
                        if(userLevel.currentLevel <= 5)
                        {
                            //Beginner
                            if(it.difficulty == "beginner")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else if(userLevel.currentLevel > 5 && userLevel.currentLevel <= 15)
                        {
                            //Intermediate
                            if(it.difficulty == "intermediate")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                        else
                        {
                            //Advanced
                            if(it.difficulty == "expert")
                            {
                                //Add to Recommendation Screen
                            }
                        }
                    }
                }
            })
        }

    }

}