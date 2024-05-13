package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class fctGuestHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fct_guest_home)

        val assign = findViewById<Button>(R.id.assign1)
        assign.setOnClickListener{
            val intent = Intent(this,GuestAssignTasks::class.java)
            startActivity(intent)
            finish()
        }

        val updateInfo = findViewById<Button>(R.id.updateinfobtn)
        updateInfo.setOnClickListener{
            val intent = Intent(this,gstEditProfile::class.java)
            intent.putExtra("Role","faculty")
            startActivity(intent)
            finish()
        }

        val manage = findViewById<Button>(R.id.manageTabtn)
        manage.setOnClickListener{
            val intent = Intent(this,gstManageTA::class.java)
            startActivity(intent)
            finish()
        }

        val feed1 = findViewById<Button>(R.id.giveFeed1)
        feed1.setOnClickListener{
            val intent = Intent(this,gstGiveFeedBack::class.java)
            startActivity(intent)
            finish()
        }

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