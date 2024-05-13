package com.tallaleatazaz.ta_ldmanagementsystem


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        val assignbtn = findViewById<Button>(R.id.assign1)
        val email1 = FirebaseAuth.getInstance().currentUser?.email
        val role1 = intent.getStringExtra("Role")

        profilebtn.setOnClickListener {
            val intent = Intent(this, ViewProfile::class.java, )
            intent.putExtra("Email",email1)
            intent.putExtra("Role",role1)
            Log.d("FacultyHome", "Current user email: ${FirebaseAuth.getInstance().currentUser?.email}")
            startActivity(intent)
        }

        homebtn.setOnClickListener {
            val intent = Intent(this, FacultyHome::class.java)
            startActivity(intent)
        }

        feedbackbtn.setOnClickListener {
            try {
                Log.d("FeedbackButton", "Button clicked")
                val intent = Intent(this, GiveFeedback::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("FeedbackButton", "Error occurred: ", e)
            }
        }

        taskbtn.setOnClickListener {
            val intent = Intent(this, AssignTasks::class.java)
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

        assignbtn.setOnClickListener {
            val intent = Intent(this, AssignTasks::class.java)
            startActivity(intent)
        }
    }

}