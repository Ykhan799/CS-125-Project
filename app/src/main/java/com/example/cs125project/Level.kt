package com.example.cs125project

class Level {

    var currentLevel = 0;
    var currentExperience = 0;
    var nextExperience = 100.00;

    fun gainExp(nextExp: Int){
        currentExperience += nextExp
        if(currentExperience >= nextExperience){
            levelUp()
        }
    }

    fun loseExp(nextExp: Int){
        currentExperience -= nextExp
        if(currentExperience < 0){
            currentExperience = 0
        }
    }

    private fun levelUp(){
        currentLevel += 1
        currentExperience = 0
        nextExperience += nextExperience * 1.15
    }
}