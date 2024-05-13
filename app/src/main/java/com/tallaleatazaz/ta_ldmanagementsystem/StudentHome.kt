package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class StudentHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        val profilebtn = findViewById<ImageButton>(R.id.profilebtn)
        val homebtn = findViewById<ImageButton>(R.id.homebtn)
        val feedbackbtn = findViewById<ImageButton>(R.id.feedbackbtn)
        val taskbtn = findViewById<ImageButton>(R.id.taskbtn)
        val updateinfobtn = findViewById<Button>(R.id.updateinfobtn)
        val email1 = FirebaseAuth.getInstance().currentUser?.email
        val role1 = intent.getStringExtra("Role")

        profilebtn.setOnClickListener {
            val intent = Intent(this, ViewProfile::class.java)
            intent.putExtra("Email",email1)
            intent.putExtra("Role",role1)
            startActivity(intent)
        }

        homebtn.setOnClickListener {
            val intent = Intent(this, StudentHome::class.java)
            startActivity(intent)
        }

        feedbackbtn.setOnClickListener {
            val intent = Intent(this, TAFeedback::class.java)
            startActivity(intent)
        }

        taskbtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        updateinfobtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
    }
}