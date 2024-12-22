package com.example.facedetectionapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.Teacher
import com.example.facedetectionapp.R
import com.example.facedetectionapp.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTeacherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)

        // Déclaration des champs de saisie et du bouton de soumission
        val etNom = findViewById<EditText>(R.id.etNom)
        val etPrenom = findViewById<EditText>(R.id.etPrenom)
        val etMatiere = findViewById<EditText>(R.id.etMatiere)
        val etFiliere = findViewById<EditText>(R.id.etFiliere)
        val etDateEmbauche = findViewById<EditText>(R.id.etDateEmbauche)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val nom = etNom.text.toString()
            val prenom = etPrenom.text.toString()
            val matiere = etMatiere.text.toString()
            val filiere = etFiliere.text.toString()
            val dateEmbauche = etDateEmbauche.text.toString()





            // Création du Teacher
            val newTeacher = Teacher(
                id = 0,  // L'ID sera généré par le backend

                nom = nom,
                prenom = prenom,
                dateEmbauche = dateEmbauche,
                matiere = matiere,
                filiere = filiere
            )

            // Appel de l'API pour ajouter l'enseignant
            val apiService = ApiService.create(this)
            apiService.addTeacher(newTeacher).enqueue(object : Callback<Teacher> {
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddTeacherActivity,
                            "Enseignant ajouté avec succès",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Fermer l'activité et revenir à la liste
                    } else {
                        Toast.makeText(
                            this@AddTeacherActivity,
                            "Erreur: " + response.code(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Toast.makeText(this@AddTeacherActivity, "Échec de l'ajout de l'enseignant", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}