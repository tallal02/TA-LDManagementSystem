package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class gstEditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gst_edit_profile)

        val role = intent.getStringExtra("Role")

        val back = findViewById<ImageView>(R.id.backArr)
        back.setOnClickListener{

                val intent = Intent(this,gstViewProfile::class.java)
                intent.putExtra("Role",role)
                startActivity(intent)
                finish()



        }

        val update = findViewById<Button>(R.id.updatebutton)
        update.setOnClickListener{
               val intent = Intent(this,gstEditProfile::class.java)
                startActivity(intent)
                finish()

        }
    }
}