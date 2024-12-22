package com.example.facedetectionapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R

class TeacherDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_dashboard)

        val btnViewClasses = findViewById<Button>(R.id.btnViewClasses)
        val btnLogoutTeacher = findViewById<Button>(R.id.btnLogoutTeacher)



        btnLogoutTeacher.setOnClickListener {
            // Supprimer le token de SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("jwt_token")  // Supprimer le token
            editor.apply()

            // Rediriger vers la page de connexion
            startActivity(Intent(this, LoginActivity::class.java))
            finish()  // Fermer l'activité actuelle
        }
    }
}