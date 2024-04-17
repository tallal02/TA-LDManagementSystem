package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    private val navigateToLogin = Runnable {
        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler.postDelayed(navigateToLogin, 5000) // Delay in milliseconds (5 seconds)
    }

    override fun onDestroy() {
        handler.removeCallbacks(navigateToLogin)
        super.onDestroy()
    }

    fun onActivityClicked(view: View) {
        handler.removeCallbacks(navigateToLogin)
        navigateToLogin.run()
    }
}

