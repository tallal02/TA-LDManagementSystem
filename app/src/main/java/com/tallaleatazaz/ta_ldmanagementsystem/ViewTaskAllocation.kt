package com.tallaleatazaz.ta_ldmanagementsystem

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.tallaleatazaz.ta_ldmanagementsystem.R
import org.json.JSONException

class ViewTaskAllocation : AppCompatActivity() {

    data class Task(
        val taskId: Int,
        val taskName: String,
        val taskDescription: String,
        val taskDeadline: String,
        val taName: String
    )

    private val taskList: MutableList<Task> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_task_allocation)

        val useremail = FirebaseAuth.getInstance().currentUser?.email
        if (useremail != null) {
            fetchTaskData(useremail)
        } else {
            Log.e("ViewTaskAllocation", "User email not found")
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
        }

        val search_query = findViewById<Button>(R.id.buttonSearch)
        val search_text = findViewById<EditText>(R.id.editTextSearch)

        search_query.setOnClickListener() {
            val search_query_text = search_text.text.toString()
            val taskLayout = findViewById<LinearLayout>(R.id.dynamicviews)
            taskLayout.removeAllViews()
            for (task in taskList) {
                if (task.taskName.contains(search_query_text, ignoreCase = true)) {
                    val taskTextView = TextView(this).apply {
                        text = buildSpannedString {
                            bold { append("TA: ") }
                            color(Color.MAGENTA) { append("${task.taName}\n") }

                            bold { append("Task ID: ") }
                            color(Color.BLUE) { append("${task.taskId}\n") }

                            bold { append("Name: ") }
                            color(Color.GREEN) { append("${task.taskName}\n") }

                            bold { append("Description: ") }
                            color(Color.RED) { append("${task.taskDescription}\n") }

                            bold { append("Deadline: ") }
                            color(Color.BLACK) { append("${task.taskDeadline}\n") }
                        }
                        textSize = 16f
                        setPadding(0, 0, 0, 20)
                    }

                    taskLayout.addView(taskTextView)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun fetchTaskData(userEmail: String) {
        val url = "http://192.168.18.28/taskallocation.php?email=$userEmail"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    Log.d("ViewTaskAllocation", "Response length: ${response.length()}")

                    val taskLayout = findViewById<LinearLayout>(R.id.dynamicviews)
                    taskLayout.removeAllViews()

                    for (i in 0 until response.length()) {
                        val task = response.getJSONObject(i)
                        Log.d("ViewTaskAllocation", "Processing task at index $i: $task")
                        try {
                            val taskId = task.getInt("task_id")
                            val taskName = task.getString("name")
                            val taskDescription = task.getString("description")
                            val taskDeadline = task.getString("deadline")
                            val taName = task.optString("ta_name", "")
                            val newTask = Task(taskId, taskName, taskDescription, taskDeadline, taName)
                            taskList.add(newTask)
                            val taskTextView = TextView(this).apply {
                                text = buildSpannedString {
                                    bold { append("TA: ") }
                                    color(Color.MAGENTA) { append("$taName\n") }

                                    bold { append("Task ID: ") }
                                    color(Color.BLUE) { append("$taskId\n") }

                                    bold { append("Name: ") }
                                    color(Color.GREEN) { append("$taskName\n") }

                                    bold { append("Description: ") }
                                    color(Color.RED) { append("$taskDescription\n") }

                                    bold { append("Deadline: ") }
                                    color(Color.BLACK) { append("$taskDeadline\n") }
                                }
                                textSize = 16f
                                setPadding(0, 0, 0, 20)
                            }

                            taskLayout.addView(taskTextView)
                            Log.d("ViewTaskAllocation", "Task TextView added successfully")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Log.e("ViewTaskAllocation", "Error processing task at index $i: ${e.message}")
                            Toast.makeText(this, "Failed to process task at index $i", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("ViewTaskAllocation", "JSON Exception: ${e.message}")
                    Toast.makeText(this, "Failed to parse task details", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("ViewTaskAllocation", "Volley Error: ${error.message}")
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            })

        requestQueue.add(jsonArrayRequest)
    }
}