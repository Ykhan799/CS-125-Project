package com.example.levelupfitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class loginPage : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page);

        var usrEmail = findViewById<EditText>(R.id.email)
        var usrPassword = findViewById<EditText>(R.id.password)
        var loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            //TODO: Make Login Button Do Something
        }
    }
}