package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ViewProfile : AppCompatActivity() {

    private lateinit var imageView : ImageView

    private var email1:String?= null


    // Firebase Storage reference
    private val storageReference = FirebaseStorage.getInstance().reference.child("user_images")

    // Firebase Realtime Database reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        val back = findViewById<ImageView>(R.id.backArr)
        val editBtn = findViewById<Button>(R.id.editBtn)
        var email2 = findViewById<TextView>(R.id.email1)
        var name1 = findViewById<TextView>(R.id.name1)
        var number1 = findViewById<TextView>(R.id.number1)
        var role2 = findViewById<TextView>(R.id.role1)

        var logout = findViewById<Button>(R.id.logout)

        imageView = findViewById(R.id.profile_image)

        email1 = intent.getStringExtra("Email")
        var role1= intent.getStringExtra("Role")

        var user = FirebaseAuth.getInstance().currentUser

        email2.text=email1
        role2.text=role1

        email1?.let { getData(it, name1, number1)
            loadImage()
        }

        back.setOnClickListener {
            if(role1=="Student") {
                val intent = Intent(this, StudentHome::class.java)
                intent.putExtra("Email",email1)
                intent.putExtra("Role",role1)
                startActivity(intent)
                finish()
            }
            else if (role1=="Faculty"){
                val intent = Intent(this, FacultyHome::class.java)
                intent.putExtra("Email",email1)
                intent.putExtra("Role",role1)
                startActivity(intent)
                finish()
            }
        }

        editBtn.setOnClickListener {
            val intent = Intent(this,EditProfile::class.java)
            intent.putExtra("Email",email1)
            intent.putExtra("Role",role1)
            startActivity(intent)
            finish()
        }

        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadImage() {
        email1?.let { email ->
            databaseReference.child("user_images").child(email.replace('.', '_')).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val imageUrl = dataSnapshot.child("imageUrl").getValue(String::class.java)
                        if (imageUrl != null) {
                            // Load image into imageViewProfile using Glide or another image loading library
                            Glide.with(this@ViewProfile)
                                .load(imageUrl)
                                .placeholder(R.drawable.profile) // Placeholder image while loading
                                .error(R.drawable.profile) // Error image if loading fails
                                .into(imageView)
                        }
                    } else {
                        // Handle case where image data does not exist for the user
                        Log.d(TAG, "No image data found for user with email: $email")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Log.e(TAG, "Database error: ${databaseError.message}")
                }
            })
        }
    }


    private fun getName(email : String, name1:TextView){
        val url = "http://192.168.18.28/getName.php"

        val requestQueue = Volley.newRequestQueue(this)

        var stringRequest = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String>{response ->
                name1.text=response
            },
            Response.ErrorListener{error->


            }) {
            override fun getParams(): MutableMap<String, String> {
                val params =  HashMap<String,String>()
                params["Email"]=email
                return params

            }
        }
        requestQueue.add(stringRequest)
    }

    private fun getNum(email:String,num1:TextView){
        val url = "http://192.168.18.28/getNumber.php"

        val requestQueue = Volley.newRequestQueue(this)

        var stringRequest = object : StringRequest (Request.Method.POST,url,Response.Listener<String> {response ->

            num1.text=response
        },
            Response.ErrorListener { error->

            }) {
            override fun getParams() : MutableMap<String,String>{
                val params = HashMap<String,String>()
                params["Email"]=email
                return params
            }



        }

        requestQueue.add(stringRequest)
    }

    private fun getData(email:String,name :TextView,number:TextView){

        val url = "http://192.168.18.28/getData1.php"

        val params = HashMap<String, String>()
        params["email"] = email

        val jsonObject = JSONObject(params as Map<*, *>?)

        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->

                if(response.getBoolean("success")) {
                    val user = response.getJSONObject("user")
                    name.text=user.getString("name")
                    number.text=user.getString("contact_number")

                    //                    name.text = response.getString("name")
//                    number.text = response.getString("contact_number")
                }
            },
            Response.ErrorListener { error ->
                // Handle error
            })

        Volley.newRequestQueue(this).add(request)
    }

}