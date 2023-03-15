package com.example.cs125project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository ): ViewModel() {

    val myReponse: MutableLiveData<Response<ExerciseData>> = MutableLiveData()
    val myReponseExerciseType: MutableLiveData<Response<ExerciseData>> = MutableLiveData()
    val myCustomPosts: MutableLiveData<Response<ExerciseData>> = MutableLiveData()

    fun getPost(){
        viewModelScope.launch {
            val response = repository.getPost()
            myReponse.value = response
        }
    }

    fun getPostExerciseType(type: String){
        viewModelScope.launch {
            val response = repository.getPostExerciseType(type)
            myReponseExerciseType.value = response
        }
    }

    fun getCustomPosts(exerciseType: String) {
        viewModelScope.launch {
            val response = repository.getCustomPosts(exerciseType)
            myCustomPosts.value = response

        }
    }

}