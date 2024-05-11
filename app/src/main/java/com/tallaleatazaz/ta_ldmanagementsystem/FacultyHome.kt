package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class FacultyHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_home)

        val profilebtn = findViewById<ImageButton>(R.id.profilebtn)
        val homebtn = findViewById<ImageButton>(R.id.homebtn)
        val feedbackbtn = findViewById<ImageButton>(R.id.feedbackbtn)
        val taskbtn = findViewById<ImageButton>(R.id.taskbtn)
        val manageTabtn = findViewById<Button>(R.id.manageTabtn)
        val updateinfobtn = findViewById<Button>(R.id.updateinfobtn)

        profilebtn.setOnClickListener {
            val intent = Intent(this, ViewProfile::class.java)
            startActivity(intent)
        }

        homebtn.setOnClickListener {
            val intent = Intent(this, FacultyHome::class.java)
            startActivity(intent)
        }

        feedbackbtn.setOnClickListener {
            val intent = Intent(this, GiveFeedback::class.java)
            startActivity(intent)
        }

        taskbtn.setOnClickListener {
            val intent = Intent(this, TaTasks::class.java)
            startActivity(intent)
        }

        manageTabtn.setOnClickListener {
            val intent = Intent(this, ManageTA::class.java)
            startActivity(intent)
        }

        updateinfobtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
    }
}