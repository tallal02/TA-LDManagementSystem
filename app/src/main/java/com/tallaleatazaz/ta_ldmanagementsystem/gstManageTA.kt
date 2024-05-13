package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class gstManageTA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gst_manage_ta)

        val prfbtn = findViewById<ImageButton>(R.id.profilebtn)
        prfbtn.setOnClickListener{
            val intent = Intent(this,gstViewProfile::class.java)
            intent.putExtra("Role","faculty")
            startActivity(intent)
            finish()
        }

        val homebtn = findViewById<ImageButton>(R.id.homebtn)
        homebtn.setOnClickListener{
            val intent = Intent(this,fctGuestHome::class.java)
            startActivity(intent)
            finish()
        }

        val feedbtn = findViewById<ImageButton>(R.id.feedbackbtn)
        feedbtn.setOnClickListener{
            val intent = Intent(this,gstGiveFeedBack::class.java)
            startActivity(intent)
            finish()
        }
    }
}