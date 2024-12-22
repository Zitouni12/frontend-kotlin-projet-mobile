package com.example.facedetectionapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R

class StudentDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_dashboard)

        val btnViewProfile = findViewById<Button>(R.id.btnViewProfile)
        val btnViewAbsences = findViewById<Button>(R.id.btnViewAbsences)
        val btnLogoutStudent = findViewById<Button>(R.id.btnLogoutStudent)

        // Action pour afficher le profil
        btnViewProfile.setOnClickListener {
            // Rediriger vers l'écran du profil de l'étudiant
            startActivity(Intent(this, StudentProfileActivity::class.java))
        }

        // Action pour afficher les absences
        btnViewAbsences.setOnClickListener {
            // Rediriger vers l'écran des absences de l'étudiant
            startActivity(Intent(this, StudentAbsenceActivity::class.java))
        }

        // Action pour se déconnecter
        btnLogoutStudent.setOnClickListener {
            // Supprimer le token JWT de SharedPreferences
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
