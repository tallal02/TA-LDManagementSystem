package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AssignTasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_tasks)

        val btn2 = findViewById<Button>(R.id.btn2)

        btn2.setOnClickListener {
            val intent = Intent(this, ViewTaskAllocation::class.java)
            startActivity(intent)
        }
    }
}