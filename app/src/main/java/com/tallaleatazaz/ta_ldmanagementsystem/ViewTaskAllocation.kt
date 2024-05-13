package com.tallaleatazaz.ta_ldmanagementsystem

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class ViewTaskAllocation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_task_allocation)
        fetchTaskData()
    }

    private fun fetchTaskData() {
        val url = "http://192.168.18.28/taskallocation.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val taskLayout = findViewById<LinearLayout>(R.id.dynamicviews)
                    taskLayout.removeAllViews()
                    for (i in 0 until response.length()) {
                        val task = response.getJSONObject(i)
                        val taskTextView = TextView(this).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).also { lp ->
                                lp.bottomMargin = 16
                            }
                            text = "Task ID: ${task.getInt("task_id")} - Name: ${task.getString("name")} - Description: ${task.getString("description")} - Deadline: ${task.getString("deadline")}"
                            textSize = 16f
                            setPadding(16, 16, 16, 16)
                        }
                        taskLayout.addView(taskTextView)
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Failed to parse task details", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            })

        requestQueue.add(jsonArrayRequest)
    }
}
