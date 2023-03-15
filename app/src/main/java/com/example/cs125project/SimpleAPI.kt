package com.example.cs125project

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleAPI {

    @GET("v1/exercises?muscle=biceps")
    suspend fun getPost(): Response<ExerciseData>

    @GET("v1/exercises?type={exerciseType}")
    suspend fun  getPostExerciseType(
        @Path("exerciseType") type: String
    ): Response<ExerciseData>

    @GET("v1/exercises")
    suspend fun getCustomPosts(
        @Query("type") exerciseType: String
    ): Response<ExerciseData>

}