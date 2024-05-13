package com.tallaleatazaz.ta_ldmanagementsystem

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException

class TAFeedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tafeedback)
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let {
            fetchFeedback(it)
            fetchUserName(it)
        } ?: Toast.makeText(this, "No email provided", Toast.LENGTH_SHORT).show()
    }

    private fun fetchUserName(email: String) {
        val url = "http://192.168.18.28/getUserName.php" // Adjust with your actual URL
        val requestQueue = Volley.newRequestQueue(this)
        val params = HashMap<String, String>()
        params["email"] = email

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val userName = response.trim() // Assuming the response is just the user's name
                    val subtitleTextView = findViewById<TextView>(R.id.subtitleTextView)
                    subtitleTextView.text = userName
                } catch (e: Exception) {
                    Log.e("TAFeedback", "Error parsing user name: $e")
                    Toast.makeText(this, "Failed to load user name", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
    private fun fetchFeedback(email: String) {
        val url = "http://192.168.18.28/getFeedback.php"
        val requestQueue = Volley.newRequestQueue(this)
        val params = HashMap<String, String>()
        params["email"] = email

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONArray(response)
                    val feedbackLayout = findViewById<LinearLayout>(R.id.dynamicFeedbackContainer)
                    feedbackLayout.removeAllViews()  // Clear previous views if any

                    for (i in 0 until jsonResponse.length()) {
                        val feedback = jsonResponse.getJSONObject(i)
                        val formattedText = formatText(feedback.getString("FacultyName"), feedback.getString("Feedback"))
                        val textView = TextView(this).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).also { lp ->
                                lp.bottomMargin = 46
                            }
                            text = formattedText
                            textSize = 14f  // Size for feedback, larger size for name set in formatText function
                            setPadding(32, 40, 32, 40)  // Increased padding for better aesthetics with larger text
                            setBackgroundResource(R.drawable.rounded_for_feedback)
                            setTextColor(getResources().getColor(R.color.black))
                            textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                        }
                        feedbackLayout.addView(textView)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to parse feedback details", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }

            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                return headers
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun formatText(name: String, feedback: String): Spanned {
        val nameStyle = "<b><big>$name</big></b>"  // Making the name bold and bigger
        return Html.fromHtml("$nameStyle<br/>$feedback", Html.FROM_HTML_MODE_LEGACY)  // Ensure feedback is on new line
    }

}
