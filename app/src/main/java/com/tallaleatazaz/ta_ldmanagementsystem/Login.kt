package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPass = findViewById<EditText>(R.id.editTextPassword)

        // Find the TextView by its ID
        val signUpTextView = findViewById<TextView>(R.id.signup)
        val loginbtn = findViewById<Button>(R.id.buttonLogin)
        // Set an OnClickListener to it

        val stdGuest = findViewById<TextView>(R.id.studentGuest)

        stdGuest.setOnClickListener {
            val intent = Intent(this,stdGuestHome::class.java)
            startActivity(intent)
            finish()
        }

        val fctGuest = findViewById<TextView>(R.id.facultyGuest)

        fctGuest.setOnClickListener {
            val intent = Intent(this,fctGuestHome::class.java)
            startActivity(intent)
            finish()
        }



        loginbtn.setOnClickListener {
            val email= editEmail.text.toString()
            val pass = editPass.text.toString()
            loginUser(email,pass)
        }

        signUpTextView.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
            finish()
        }





    }

    private fun loginUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, get current user
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        // User is logged in, get user's role
                        getUserRole(user.uid,email)
                    }
                } else {
                    // Login failed, display error message
                    Toast.makeText(
                        applicationContext,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun getUserRole(userId: String , email :String) {
        // Retrieve user's role from Firestore based on userId
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userRole = document.getString("role")
                    if (userRole != null) {
                        // Navigate to respective screen based on user's role
                        if (userRole == "Student") {
                            val intent = Intent(this, StudentHome::class.java)
                            intent.putExtra("Email",email)
                            intent.putExtra("Role",userRole)
                            startActivity(intent)
                            finish()
                        } else if (userRole == "Faculty") {
                            val intent = Intent(this, FacultyHome::class.java)
                            intent.putExtra("Email",email)
                            intent.putExtra("Role",userRole)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // User role not found, handle appropriately
                        Toast.makeText(
                            applicationContext,
                            "User role not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Document not found, handle appropriately
                    Toast.makeText(
                        applicationContext,
                        "User document not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Error occurred while retrieving user's role, handle appropriately
                Toast.makeText(
                    applicationContext,
                    "Error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}