package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private var isSigningUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val spinnerRole = findViewById<Spinner>(R.id.editTextRole)
        ArrayAdapter.createFromResource(
            this,
            R.array.role_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRole.adapter = adapter
        }

        val emailText = findViewById<EditText>(R.id.editTextEmail)
        val nameText = findViewById<EditText>(R.id.editTextName)
        val passText = findViewById<EditText>(R.id.editTextPassword)
        val contactText = findViewById<EditText>(R.id.editTextContact)

        val btnSignup = findViewById<Button>(R.id.buttonSignUp)
        btnSignup.setOnClickListener {
            if (!isSigningUp) {
                isSigningUp = true
                val email = emailText.text.toString()
                val name = nameText.text.toString()
                val pass = passText.text.toString()
                val contact = contactText.text.toString()
                val role = spinnerRole.selectedItem.toString()

                signUp(email, name, pass, contact, role)
            }
        }
    }

    private fun signUp(email: String, name: String, pass: String, contact: String, role: String) {
        val url = "http://192.168.18.237/signup.php"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("SignUp", "Server Response: $response")
                signUpFirebase(email, pass, role)
            },
            Response.ErrorListener { error ->
                val message = error.message ?: "Unknown error"
                Log.e("SignUp", "Network Error: $error")
                Toast.makeText(this@SignUp, "Error occurred: $message", Toast.LENGTH_SHORT).show()
                isSigningUp = false
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["email"] = email
                params["password"] = pass
                params["role"] = role
                params["contact_number"] = contact

                Log.d("SignUp", "Params: $params")
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    fun signUpFirebase(email: String, password: String, role: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    firebaseUser?.let { user ->
                        val userRole = hashMapOf("role" to role)
                        FirebaseFirestore.getInstance().collection("users")
                            .document(user.uid)
                            .set(userRole)
                            .addOnSuccessListener {
                                if (role == "Student") {
                                    val intent = Intent(this, StudentHome::class.java)
                                    startActivity(intent)
                                    finish()
                                } else if (role == "Faculty") {
                                    val intent = Intent(this, FacultyHome::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                isSigningUp = false
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirebaseFirestore", "Failed to save user role: ${e.message}")
                                Toast.makeText(this, "Failed to save user role: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                isSigningUp = false
                            }
                    }
                } else {
                    task.exception?.let {
                        Log.e("FirebaseAuth", "Signup failed: ${it.message}")
                        Toast.makeText(this, "Firebase signup failed: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                        isSigningUp = false
                    }
                }
            }
    }
}
