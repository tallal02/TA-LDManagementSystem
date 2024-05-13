package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class stdGuestHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_std_guest_home)

        val viewTasks = findViewById<Button>(R.id.assign1)
        viewTasks.setOnClickListener{
            val intent = Intent(this,gstTaTasks::class.java)
            startActivity(intent)
            finish()
        }

        val updateInfo = findViewById<Button>(R.id.updateinfobtn)
        updateInfo.setOnClickListener{
            val intent = Intent(this,gstEditProfile::class.java)
            intent.putExtra("Role","student")
            startActivity(intent)
            finish()
        }

        val feed1 = findViewById<Button>(R.id.giveFeed1)
        feed1.setOnClickListener{
            val intent = Intent(this,gstTaFeedBack::class.java)
            startActivity(intent)
            finish()
        }



        val prfbtn = findViewById<ImageButton>(R.id.profilebtn)
        prfbtn.setOnClickListener{
            val intent = Intent(this,gstViewProfile::class.java)
            intent.putExtra("Role","student")
            startActivity(intent)
            finish()
        }

        val homebtn = findViewById<ImageButton>(R.id.homebtn)
        homebtn.setOnClickListener{
            val intent = Intent(this,stdGuestHome::class.java)
            startActivity(intent)
            finish()
        }

        val feedbtn = findViewById<ImageButton>(R.id.feedbackbtn)
        feedbtn.setOnClickListener{
            val intent = Intent(this,gstTaFeedBack::class.java)
            startActivity(intent)
            finish()
        }

        val tasksbtn = findViewById<ImageButton>(R.id.taskbtn)
        tasksbtn.setOnClickListener{
            val intent = Intent(this,gstTaTasks::class.java)
            startActivity(intent)
            finish()
        }

    }
}