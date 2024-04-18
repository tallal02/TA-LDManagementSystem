package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find the TextView by its ID
        val signUpTextView = findViewById<TextView>(R.id.signup)

        // Set an OnClickListener to it
        signUpTextView.setOnClickListener {
            // Create an Intent to start the SignUp activity
            val intent = Intent(this, ViewProfile::class.java)
            // Start the SignUp activity
            startActivity(intent)
        }
    }
}