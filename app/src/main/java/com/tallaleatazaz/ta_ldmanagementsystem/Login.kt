package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find the TextView by its ID
        val signUpTextView = findViewById<TextView>(R.id.signup)
        val loginbtn = findViewById<Button>(R.id.buttonLogin)
        // Set an OnClickListener to it
        loginbtn.setOnClickListener {
            // Create an Intent to start the SignUp activity
            val intent = Intent(this, StudentHome::class.java)
            // Start the SignUp activity
            startActivity(intent)
        }
    }
}