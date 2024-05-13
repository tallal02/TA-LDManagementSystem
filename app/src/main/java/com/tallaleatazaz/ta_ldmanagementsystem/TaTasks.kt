package com.tallaleatazaz.ta_ldmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException

class TaTasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ta_tasks)
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        userEmail?.let {
            fetchTasks(it)
        } ?: Toast.makeText(this, "No email provided", Toast.LENGTH_SHORT).show()
    }

    private fun fetchTasks(email: String) {
        val url = "http://192.168.18.28/view_own_tasks.php?email=$email"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    val tasksArray = JSONArray(response)
                    val tasksLayout = findViewById<LinearLayout>(R.id.dynamic_tasks)
                    tasksLayout.removeAllViews() // Clear previous views if any

                    for (i in 0 until tasksArray.length()) {
                        val task = tasksArray.getJSONObject(i)
                        val formattedText = formatTaskText(
                            task.getString("name"),
                            task.getString("description"),
                            task.getString("deadline"),
                            task.getString("course_name")
                        )

                        val taskTextView = TextView(this).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).also { lp ->
                                lp.bottomMargin = 20
                            }
                            text = formattedText
                            textSize = 16f
                            setPadding(20, 20, 20, 20)
                        }

                        tasksLayout.addView(taskTextView)
                    }
                } catch (e: JSONException) {
                    Log.e("TaTasks", "Error parsing tasks: $e")
                    Toast.makeText(this, "Failed to parse tasks", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("TaTasks", "Error fetching tasks: ${error.message}")
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            })

        requestQueue.add(stringRequest)
    }

    private fun formatTaskText(name: String, description: String, deadline: String, courseName: String): String {
        return "Task Name: $name\nDescription: $description\nDeadline: $deadline\nCourse Name: $courseName"
    }
}
