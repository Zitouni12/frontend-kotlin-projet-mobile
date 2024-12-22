package com.example.facedetectionapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.models.Teacher
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageTeacherEditActivity : AppCompatActivity() {

    private lateinit var etNom: EditText
    private lateinit var etPrenom: EditText
    private lateinit var etDateEmbauche: EditText
    private lateinit var etMatiere: EditText
    private lateinit var etFiliere: EditText
    //private lateinit var etUsername: EditText
    //private lateinit var etPassword: EditText
    private lateinit var etRole: EditText
    private lateinit var btnSave: Button

    private var teacherId: Int = 0  // ID de l'enseignant à modifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_teacher_edit)

        // Initialisation des champs de saisie et du bouton de soumission
        etNom = findViewById(R.id.etNom)
        etPrenom = findViewById(R.id.etPrenom)
        etDateEmbauche = findViewById(R.id.etDateEmbauche)
        etMatiere = findViewById(R.id.etMatiere)
        etFiliere = findViewById(R.id.etFiliere)

        btnSave = findViewById(R.id.btnSave)

        // Récupérer l'ID de l'enseignant à modifier
        teacherId = intent.getIntExtra("teacherId", 0)

        // Récupérer les détails de l'enseignant
        fetchTeacherDetails()

        // Enregistrer les modifications
        btnSave.setOnClickListener {
            val nom = etNom.text.toString()
            val prenom = etPrenom.text.toString()
            val dateEmbauche = etDateEmbauche.text.toString()
            val matiere = etMatiere.text.toString()
            val filiere = etFiliere.text.toString()

           // val role = etRole.text.toString()



            // Création du Teacher mis à jour
            val updatedTeacher = Teacher(
                id = teacherId,

                nom = nom,
                prenom = prenom,
                dateEmbauche = dateEmbauche,
                matiere = matiere,
                filiere = filiere
            )

            // Appel de l'API pour mettre à jour l'enseignant
            val apiService = ApiService.create(this)
            apiService.updateTeacher(teacherId, updatedTeacher).enqueue(object : Callback<Teacher> {
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ManageTeacherEditActivity, "Enseignant mis à jour avec succès", Toast.LENGTH_SHORT).show()
                        finish() // Fermer l'activité et revenir à la liste
                    } else {
                        Toast.makeText(this@ManageTeacherEditActivity, "Erreur: " + response.code(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Toast.makeText(this@ManageTeacherEditActivity, "Échec de la mise à jour de l'enseignant", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun fetchTeacherDetails() {
        val apiService = ApiService.create(this)
        apiService.getTeacherById(teacherId).enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if (response.isSuccessful) {
                    val teacher = response.body()
                    if (teacher != null) {
                        // Remplir les champs avec les données existantes
                        etNom.setText(teacher.nom)
                        etPrenom.setText(teacher.prenom)
                        etDateEmbauche.setText(teacher.dateEmbauche)
                        etMatiere.setText(teacher.matiere)
                        etFiliere.setText(teacher.filiere)

                    }
                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                Toast.makeText(this@ManageTeacherEditActivity, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
