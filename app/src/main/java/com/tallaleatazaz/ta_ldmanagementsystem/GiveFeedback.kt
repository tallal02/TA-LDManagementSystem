package com.tallaleatazaz.ta_ldmanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class GiveFeedback : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_feedback)

        val feedbackEditText = findViewById<EditText>(R.id.edit3)
        val taUserEditText = findViewById<EditText>(R.id.edit1)
        val courseNameEditText = findViewById<EditText>(R.id.edit2) // Assuming edit2 is for course name
        val submitButton = findViewById<Button>(R.id.btn1)
        val responseTextView = findViewById<TextView>(R.id.responseTextView)

        submitButton.setOnClickListener {
            val feedbackText = feedbackEditText.text.toString()
            val taEmail = taUserEditText.text.toString()
            val courseName = courseNameEditText.text.toString() // Retrieve the course name

            if (feedbackText.isEmpty() || taEmail.isEmpty() || courseName.isEmpty()) {
                responseTextView.text = "Please fill in all fields."
            } else {
                submitFeedback(taEmail, courseName, feedbackText, responseTextView)
                submitButton.isEnabled = false // Disable the button to prevent multiple submissions
            }
        }
    }

    private fun submitFeedback(taEmail: String, courseName: String, feedbackText: String, responseTextView: TextView) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.18.237/submit_feedback.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Toast.makeText(this, "Feedback Successful: $response", Toast.LENGTH_LONG).show()
                findViewById<Button>(R.id.btn1).isEnabled = true // Re-enable the button after response
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Feedback Failed: ${error.message}", Toast.LENGTH_LONG).show()
                findViewById<Button>(R.id.btn1).isEnabled = true // Re-enable the button after error
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["ta_email"] = taEmail
                params["course_name"] = courseName
                params["feedback_text"] = feedbackText
                val facultyUserId = "1" // This should be dynamically obtained
                params["faculty_user_id"] = facultyUserId
                return params
            }
        }

        queue.add(stringRequest)
    }

}
