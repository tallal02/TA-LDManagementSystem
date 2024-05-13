package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
//import kotlin.io.encoding.Base64

import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
//import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONException
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.UUID


class EditProfile : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView

    private var email1: String? = null

    // Firebase Storage reference
    private val storageReference = FirebaseStorage.getInstance().reference.child("user_images")

    // Firebase Realtime Database reference
    private val databaseReference = FirebaseDatabase.getInstance().reference



    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        imageViewProfile = findViewById(R.id.profile_image)

        val user = FirebaseAuth.getInstance().currentUser

        email1 = intent.getStringExtra("Email")
        val role1 = intent.getStringExtra("Role")

        val back = findViewById<ImageView>(R.id.backArr)

        var name1 = findViewById<EditText>(R.id.editTextName)
        var pass1 = findViewById<EditText>(R.id.editTextPassword)
        var num1 = findViewById<EditText>(R.id.editTextContact)

        var update = findViewById<Button>(R.id.updatebutton)





        update.setOnClickListener {
            var name = name1.text.toString()
            var pass = pass1.text.toString()
            var num = num1.text.toString()

            email1?.let { setData(it, name, pass, num) }


        }

        back.setOnClickListener {
            val intent = Intent(this,ViewProfile::class.java)
            intent.putExtra("Email",email1)
            intent.putExtra("Role",role1)
            startActivity(intent)
            finish()
        }

        imageViewProfile.setOnClickListener {
            selectImage()
        }


    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            imageViewProfile.setImageBitmap(bitmap)
            email1?.let { uploadImage(it, bitmap) }
        }
    }

    private fun uploadImage(email: String, bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        // Generate a unique filename for the image
        val filename = "${UUID.randomUUID()}.jpg"

        // Upload image to Firebase Storage
        val imageRef = storageReference.child(filename)
        val uploadTask = imageRef.putBytes(imageBytes)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save image URL to Firebase Realtime Database
                val imageData = mapOf(
                    "email" to email,
                    "imageUrl" to uri.toString()
                )
                databaseReference.child("user_images").child(email.replace('.', '_')).setValue(imageData)
                Toast.makeText(applicationContext, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                // Handle failures
                Log.e(TAG, "Error getting image download URL: $exception")
                Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            // Handle failures
            Log.e(TAG, "Error uploading image: $exception")
            Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(email1:String,name:String,pass:String,num:String){

        if(name.isNotBlank()){
            setName(email1,name)
        }

        if(num.isNotBlank()){
            setNum(email1,num)
        }

        if(pass.isNotBlank()){
            setPass(email1,pass)
        }



        val user = FirebaseAuth.getInstance().currentUser
        user?.let {

            // Update password
            if (pass.isNotBlank()) {
                user.updatePassword(pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Password updated successfully in Firebase Authentication")
                    } else {
                        Log.e(TAG, "Failed to update password in Firebase Authentication", task.exception)
                    }
                }
            }
        }


    }

    private fun setName(email1:String,name: String){
        val url = "http://192.168.18.28/setName.php"

        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST,url, Response.Listener<String>{ response->

                Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener{error->

            }) {
            override fun getParams():MutableMap<String,String>{
                val params = HashMap<String,String>()
                params["email1"]=email1
                params["name"]=name
                return params
            }

        }

        requestQueue.add(stringRequest)
    }

    private fun setNum(email1:String,num: String){
        val url = "http://192.168.18.28/setNum.php"

        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST,url, Response.Listener<String>{ response->

                Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener{error->

            }) {
            override fun getParams():MutableMap<String,String>{
                val params = HashMap<String,String>()
                params["email1"]=email1
                params["num"]=num
                return params
            }

        }

        requestQueue.add(stringRequest)
    }

    private fun setPass(email1:String,pass: String){
        val url = "http://192.168.18.28/setPass.php"

        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST,url, Response.Listener<String>{ response->

            },
            Response.ErrorListener{error->

            }) {
            override fun getParams():MutableMap<String,String>{
                val params = HashMap<String,String>()
                params["email1"]=email1
                params["pass"]=pass
                return params
            }

        }

        requestQueue.add(stringRequest)
    }


}