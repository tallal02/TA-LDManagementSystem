package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class gstViewProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gst_view_profile)

        val role = intent.getStringExtra("Role")

        val back = findViewById<ImageView>(R.id.backArr)
        back.setOnClickListener{
            if(role=="student"){
                val intent = Intent(this,stdGuestHome::class.java)
                startActivity(intent)
                finish()
            }

            else if (role=="faculty"){
                val intent = Intent(this,fctGuestHome::class.java)
                startActivity(intent)
                finish()
            }

        }

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener{

                val intent = Intent(this,Login::class.java)
                startActivity(intent)
                finish()


        }

        val edit = findViewById<Button>(R.id.editBtn)
        edit.setOnClickListener{
            if(role=="student"){
                val intent = Intent(this,gstEditProfile::class.java)
                intent.putExtra("Role","student")
                startActivity(intent)
                finish()
            }

            else if (role=="faculty"){
                val intent = Intent(this,gstEditProfile::class.java)
                intent.putExtra("Role","faculty")
                startActivity(intent)
                finish()
            }

        }






    }
}