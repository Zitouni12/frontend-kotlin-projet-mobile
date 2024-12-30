package com.example.facedetectionapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.LoginRequest
import com.example.facedetectionapp.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(username, password)
            val apiService = ApiService.create(this)  // Vérifiez si ApiService est bien configuré

            apiService?.login(loginRequest)?.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.access != null) {
                            // Sauvegarde du token et rôle
                            val sharedPreferences =
                                getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().apply {
                                putString("jwt_token", loginResponse.access)
                                putString("role", loginResponse.role)
                                //putString("student_id", loginResponse.studentId)  // Ajouter studentId
                                apply()
                            }

                            // Redirection selon le rôle
                            when (loginResponse.role) {
                                "Admin" -> startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        AdminDashboardActivity::class.java
                                    )
                                )

                                "Enseignant" -> startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        TeacherDashboardActivity::class.java
                                    )
                                )

                                "Etudiant" -> startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        StudentDashboardActivity ::class.java
                                    )
                                )

                                else -> Toast.makeText(
                                    this@LoginActivity,
                                    "Rôle inconnu",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Réponse invalide",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Erreur HTTP: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Erreur réseau: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    t.printStackTrace()  // Log complet pour débogage
                }
            })
        }   }
}
