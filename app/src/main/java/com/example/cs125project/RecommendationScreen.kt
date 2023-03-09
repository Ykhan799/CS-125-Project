package com.example.cs125project

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_recommendations.*
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cs125project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.api-ninjas.com/"
class RecommendationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        getExerciseData()
    }

    private fun getExerciseData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(APIInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<ExerciseDataItem>?> {
            override fun onResponse(
                call: Call<List<ExerciseDataItem>?>,
                response: Response<List<ExerciseDataItem>?>
            ) {
                val responseBody = response.body()!!

                val myStringBuilder = StringBuilder()
                for (exerciseData in responseBody) {
                    myStringBuilder.append(exerciseData.name)
                    myStringBuilder.append("\n")
                }

                textView.text = myStringBuilder
            }

            override fun onFailure(call: Call<List<ExerciseDataItem>?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })
    }
}