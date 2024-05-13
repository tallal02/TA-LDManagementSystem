package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging

class GiveFeedback : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: FeedbackDbHelper
    private lateinit var devToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_feedback)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val feedbackEditText = findViewById<EditText>(R.id.edit3)
        val taUserEditText = findViewById<EditText>(R.id.edit1)
        val courseNameEditText = findViewById<EditText>(R.id.edit2)
        val submitButton = findViewById<Button>(R.id.btn1)
        val responseTextView = findViewById<TextView>(R.id.responseTextView)
        val profilebtn = findViewById<ImageButton>(R.id.profilebtn)
        val homebtn = findViewById<ImageButton>(R.id.homebtn)
        val feedbackbtn = findViewById<ImageButton>(R.id.feedbackbtn)
        val taskbtn = findViewById<ImageButton>(R.id.taskbtn)

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
            val intent = Intent(this, AssignTasks::class.java)
            startActivity(intent)
        }
        devToken = sharedPreferences.getString("device_token", "") ?: ""

        dbHelper = FeedbackDbHelper(this)

        submitButton.setOnClickListener {
            val feedbackText = feedbackEditText.text.toString()
            val taEmail = taUserEditText.text.toString()
            val courseName = courseNameEditText.text.toString()

            if (feedbackText.isEmpty() || taEmail.isEmpty() || courseName.isEmpty()) {
                responseTextView.text = "Please fill in all fields."
            } else {
                val deviceToken = sharedPreferences.getString("device_token", null)

                if (deviceToken != null) {
                    submitFeedback(taEmail, courseName, feedbackText, deviceToken, responseTextView)
                } else {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val newDeviceToken = task.result
                            sharedPreferences.edit().putString("device_token", newDeviceToken).apply()
                            submitFeedback(taEmail, courseName, feedbackText, newDeviceToken, responseTextView)
                        } else {
                            Log.e("GiveFeedback", "Failed to fetch device token: ${task.exception}")
                        }
                    }
                }

                submitButton.isEnabled = false
            }
        }

        // Sync data with server when internet connection is available
        if (isNetworkAvailable()) {
            syncDataWithServer()
        }
    }

    override fun onResume() {
        super.onResume()
        // Check for network connectivity and sync data with server if available
        if (isNetworkAvailable()) {
            syncDataWithServer()
        }
    }

    private fun submitFeedback(taEmail: String, courseName: String, feedbackText: String, deviceToken: String, responseTextView: TextView) {
        if (isNetworkAvailable()) {
            submitFeedbackToServer(taEmail, courseName, feedbackText, deviceToken, responseTextView)
        } else {
            dbHelper.insertFeedback(taEmail, courseName, feedbackText)
            Toast.makeText(this, "Feedback stored locally", Toast.LENGTH_LONG).show()
        }
    }

    private fun submitFeedbackToServer(taEmail: String, courseName: String, feedbackText: String, deviceToken: String, responseTextView: TextView) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.18.28/submit_feedback.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                Toast.makeText(this, "Feedback Successful: $response", Toast.LENGTH_LONG).show()
                if(response.contains("success")) {
                    sendNotification(deviceToken)
                }
                findViewById<Button>(R.id.btn1).isEnabled = true
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Feedback Failed: ${error.message}", Toast.LENGTH_LONG).show()
                findViewById<Button>(R.id.btn1).isEnabled = true
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["ta_email"] = taEmail
                params["course_name"] = courseName
                params["feedback_text"] = feedbackText
                val facultyUserId = "1"
                params["faculty_user_id"] = facultyUserId
                return params
            }
        }
        queue.add(stringRequest)
    }

    private fun sendNotification(deviceToken: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.18.28/sendNotification.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("Notification", "Notification sent successfully: $response")
            },
            Response.ErrorListener { error ->
                Log.e("Notification", "Error sending notification: ${error.message}")
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["device_token"] = deviceToken
                params["title"] = "New Feedback Received"
                params["message"] = "You have received new feedback."
                return params
            }
        }
        queue.add(stringRequest)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun syncDataWithServer() {
        val unsentFeedback = dbHelper.getUnsentFeedback()

        for (feedback in unsentFeedback) {
            submitFeedbackToServer(feedback.taEmail, feedback.courseName, feedback.feedbackText, devToken, findViewById<TextView>(R.id.responseTextView))
            dbHelper.deleteFeedback(feedback.id)
        }
    }
}
