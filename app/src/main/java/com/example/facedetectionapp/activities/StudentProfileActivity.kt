package com.example.facedetectionapp.activities

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.util.Log

class StudentProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvBirthDate: TextView
    private lateinit var ivProfileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        tvName = findViewById(R.id.tvName)
        tvBirthDate = findViewById(R.id.tvBirthDate)
        ivProfileImage = findViewById(R.id.ivProfileImage)

        val sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        token?.let {
            fetchStudentProfile(it)
        } ?: run {
            Toast.makeText(this, "Token manquant, veuillez vous reconnecter.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchStudentProfile(token: String) {
        val apiService = ApiService.create(this)

        apiService.getStudentProfile("Bearer $token")?.enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    profile?.let {
                        tvName.text = "${it.firstName} ${it.lastName}"
                        tvBirthDate.text = it.birthDate

                        // Complétez l'URL relative
                        val baseUrl = "http://192.168.1.106:8000" // Remplacez par l'URL de votre serveur
                        val imageUrl = "$baseUrl${it.image}"
                        Log.d("StudentProfileActivity", "Image URL: $imageUrl")

                        // Charger l'image avec Glide
                        Glide.with(this@StudentProfileActivity)
                            .load(imageUrl)
                            .error(R.drawable.ic_student_placeholder)
                            .into(ivProfileImage)
                    }
                } else {
                    Toast.makeText(this@StudentProfileActivity, "Erreur lors du chargement du profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@StudentProfileActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}
