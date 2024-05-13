package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ManageTA : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_ta)

        requestQueue = Volley.newRequestQueue(this)

        val course_id = findViewById<EditText>(R.id.edit5)
        val ta_id = findViewById<EditText>(R.id.subjectID)
        val section = findViewById<EditText>(R.id.taskname)
        val assgn_btn = findViewById<Button>(R.id.btn1)

        assgn_btn.setOnClickListener {
            // Check if all fields are filled
            if (course_id.text.isNotBlank() && ta_id.text.isNotBlank() && section.text.isNotBlank()) {
                val courseId = course_id.text.toString()
                val taId = ta_id.text.toString()
                val sectionVal = section.text.toString()

                // Call function to send data to PHP script
                sendDataToPhp(courseId, taId, sectionVal)
            } else {
                // Show a toast message indicating that all fields are required
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendDataToPhp(courseId: String, taId: String, sectionVal: String) {
        val url = "http://192.168.18.28/manage_ta.php" // Replace "your_domain" with your actual domain

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response from the server, if needed
                // For now, let's just show a toast indicating success
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["course_id"] = courseId
                params["ta_id"] = taId
                params["section"] = sectionVal
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}