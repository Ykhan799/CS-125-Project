package com.example.cs125project

import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<ExerciseData> {
        return RetrofitInstance.api.getPost()
    }

    suspend fun getPostExerciseType(type: String): Response<ExerciseData> {
        return RetrofitInstance.api.getPostExerciseType(type)
    }

    suspend fun getCustomPosts(exerciseType: String, offset: Int): Response<ExerciseData> {
        return RetrofitInstance.api.getCustomPosts(exerciseType, offset)
    }
}