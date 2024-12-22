package com.example.facedetectionapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R

class AdminDashboardActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer les préférences partagées
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userRole = sharedPreferences.getString("role", null)

        // Ajouter un log pour vérifier le rôle
        Log.d("AdminDashboard", "Rôle utilisateur récupéré: $userRole")

        // Vérifier si l'utilisateur est un admin, sinon fermer l'activité
        if (userRole != "Admin") {
            //Log et redirection vers la page de connexion
            Log.d("AdminDashboard", "Utilisateur non autorisé, redirection vers login")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.admin_dashboard)

        val showStudentsButton = findViewById<LinearLayout>(R.id.showStudentsButton)
        val showTeachersButton = findViewById<LinearLayout>(R.id.showTeachersButton)
        val btnLogoutAdmin = findViewById<LinearLayout>(R.id.btnLogoutAdmin)

        // Action pour gérer les enseignants
        showTeachersButton.setOnClickListener {
            val intent = Intent(this, ManageTeacherActivity::class.java)
            startActivity(intent)
        }

        // Action pour gérer les étudiants
        showStudentsButton.setOnClickListener {
            try {
                val intent = Intent(this, ManageStudentsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("AdminDashboard", "Erreur lors du lancement de ManageStudentsActivity", e)
                Toast.makeText(this, "Erreur lors de la redirection", Toast.LENGTH_SHORT).show()
            }
        }


        // Action pour se déconnecter
        btnLogoutAdmin.setOnClickListener {
            // Supprimer les préférences liées à l'authentification
            val editor = sharedPreferences.edit()
            editor.clear() // Vide toutes les préférences liées à l'authentification
            editor.apply()

            // Affiche un Toast et redirige vers la page de connexion
            Toast.makeText(this, "Vous êtes déconnecté.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
