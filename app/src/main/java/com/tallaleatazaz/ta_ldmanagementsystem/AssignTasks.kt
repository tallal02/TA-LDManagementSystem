package com.tallaleatazaz.ta_ldmanagementsystem


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class AssignTasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_tasks)

        // Initialize views
        val taskNameEditText = findViewById<EditText>(R.id.edit1)
        val taskDescEditText = findViewById<EditText>(R.id.edit3)
        val taskDeadlineEditText = findViewById<EditText>(R.id.deadline)
        val courseIdEditText = findViewById<EditText>(R.id.edit2)
        val viewBtn = findViewById<Button>(R.id.btn1)
        val view_task_allocation_btn = findViewById<Button>(R.id.Task_allocations)

        view_task_allocation_btn.setOnClickListener {
            val intent = Intent(this, ViewTaskAllocation::class.java)
            startActivity(intent)
        }

        // Handle button click
        viewBtn.setOnClickListener {
            // Get data from EditText fields
            val name = taskNameEditText.text.toString().trim()
            val description = taskDescEditText.text.toString().trim()
            val deadline = taskDeadlineEditText.text.toString().trim()
            val courseId = courseIdEditText.text.toString().trim()

            // Send data to PHP script using Volley library
            val url = "http://192.168.18.28/assign_task.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    // Handle response from PHP script
                    try {
                        val jsonObject = JSONObject(response)
                        val message = jsonObject.getString("message")
                        // Show message to the user
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        // You can handle success/failure scenarios here
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        // Handle JSON parsing error
                        Toast.makeText(this, "JSON parsing error", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    // Handle Volley error
                    error.printStackTrace()
                    // Show error message to the user
                    Toast.makeText(this, "Volley error", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    // Add POST parameters
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["description"] = description
                    params["deadline"] = deadline
                    params["course_id"] = courseId
                    return params
                }
            }

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}