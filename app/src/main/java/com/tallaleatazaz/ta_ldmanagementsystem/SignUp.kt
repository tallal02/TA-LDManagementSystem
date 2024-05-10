package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
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

        var emailText = findViewById<EditText>(R.id.editTextEmail)
        var nameText = findViewById<EditText>(R.id.editTextName)
        var passText = findViewById<EditText>(R.id.editTextPassword)
        var contactText = findViewById<EditText>(R.id.editTextContact)

        var btnSignup = findViewById<Button>(R.id.buttonSignUp)

        btnSignup.setOnClickListener {
           var email = emailText.text.toString()
           var name = nameText.text.toString()
           var pass = passText.text.toString()
           var contact = contactText.text.toString()
           var role = spinnerRole.selectedItem.toString()

           signUp(email,name,pass,contact,role)



        }

    }


    private fun signUp(email:String,name:String,pass:String,contact:String,role:String){
        val url = "http://10.0.2.2/signup.php"

        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String>{ response->

                                     signUpFirebase(email,pass,role)


                                     //            if(role == "Student") {
//                val intent = Intent(this, StudentHome::class.java)
//                startActivity(intent)
//                finish()
//            }
//            else if(role == "Faculty") {
//                val intent = Intent(this, FacultyHome::class.java)
//                startActivity(intent)
//                finish()
//            }


        },Response.ErrorListener{error->
                Toast.makeText(applicationContext, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()

            }) {
            override fun getParams() : MutableMap<String,String>{
                val params = HashMap<String,String>()
                params["name"]=name
                params["email"]=email
                params["password"]=pass
                params["role"]=role
                params["contact_number"]=contact




                return params
            }

        }

        requestQueue.add(stringRequest)
    }

    // Function to sign up a user
    // Function to sign up a user
    fun signUpFirebase(email: String, password: String, role: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User signed up successfully
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    firebaseUser?.let { user ->
                        // Store additional user information including role in Firebase Realtime Database or Firestore
                        val userRole = hashMapOf(
                            "role" to role
                            // Add other user information as needed
                        )
                        FirebaseFirestore.getInstance().collection("users")
                            .document(user.uid)
                            .set(userRole)
                            .addOnSuccessListener {
                                // Role assigned successfully
                                // You can navigate to the appropriate screen based on the role here
                                if (role == "Student") {
                                    // Navigate to StudentHomeActivity
                                    val intent = Intent(this,StudentHome::class.java)
                                    startActivity(intent)
                                    finish()
                                } else if (role == "Faculty") {
                                    // Navigate to FacultyHomeActivity
                                    val intent = Intent(this,FacultyHome::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle failure to store user information
                            }
                    }
                } else {
                    // Handle sign up failure
                }
            }
    }


}