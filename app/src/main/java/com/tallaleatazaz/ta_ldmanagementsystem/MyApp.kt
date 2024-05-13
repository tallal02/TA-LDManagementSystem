package com.tallaleatazaz.ta_ldmanagementsystem

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable Firebase persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}

