package com.example.cs125project

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_recommendations.*
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cs125project.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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


    // Child names
    private val heightChild = "Height"
    private val weightChild = "Weight"
    private val ageChild = "Age"
    private val preferencesChild = "Preferences"
    private val option1Child = "Option 1"
    private val option2Child = "Option 2"
    private val option3Child = "Option 3"
    private val option4Child = "Option 4"
    private val levelChild = "Level"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser!!.email.toString().substringBefore("@")


        //Get user Context
        //TODO: Add Level to User Context
        val userReference = databaseReference.child(currentUser)
        userReference.get().addOnSuccessListener {
            if(it.exists()){
                val userHeight = it.child(heightChild).value
                val userWeight = it.child(weightChild).value
                val userAge = it.child(ageChild).value
                val buildMuscle = it.child(preferencesChild).child(option1Child).value
                val gainWeight = it.child(preferencesChild).child(option2Child).value
                val loseWeight = it.child(preferencesChild).child(option3Child).value
                val improveFlexibility = it.child(preferencesChild).child(option4Child).value
                var userLevel = it.child(levelChild).value
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

    }

}