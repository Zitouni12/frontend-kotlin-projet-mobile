package com.example.facedetectionapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.Teacher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageTeacherActivity : AppCompatActivity(), TeacherAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_teachers)

        recyclerView = findViewById(R.id.rvTeachers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        teacherAdapter = TeacherAdapter(emptyList(), this)
        recyclerView.adapter = teacherAdapter

        // Ajouter un gestionnaire de clic pour le bouton "Add Teacher"
        val btnAddTeacher = findViewById<Button>(R.id.btnAddTeacher)
        btnAddTeacher.setOnClickListener {
            val intent = Intent(this, AddTeacherActivity::class.java)
            startActivity(intent)
        }

        // Récupérer la liste des enseignants
        fetchTeachers()
    }

    private fun fetchTeachers() {
        val apiService = ApiService.create(this)
        apiService.getTeachers().enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful) {
                    val teachers = response.body() ?: emptyList()
                    teacherAdapter.updateTeachers(teachers)
                } else {
                    Toast.makeText(this@ManageTeacherActivity, "Erreur lors de la récupération des enseignants", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                // Gérer l'erreur
                t.printStackTrace()
                Toast.makeText(this@ManageTeacherActivity, "Erreur de connexion. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Implémentation des méthodes du OnItemClickListener
    override fun onEditClick(teacher: Teacher) {
        val intent = Intent(this, ManageTeacherEditActivity::class.java)
        intent.putExtra("teacherId", teacher.id)
        startActivity(intent)
    }

    override fun onDeleteClick(teacher: Teacher) {
        deleteTeacher(teacher)
    }

    private fun deleteTeacher(teacher: Teacher) {
        val apiService = ApiService.create(this)
        apiService.deleteTeacher(teacher.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ManageTeacherActivity, "Enseignant supprimé", Toast.LENGTH_SHORT).show()
                    fetchTeachers() // Recharger la liste après suppression
                } else {
                    Toast.makeText(this@ManageTeacherActivity, "Erreur lors de la suppression de l'enseignant", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Gérer l'erreur
                t.printStackTrace()
                Toast.makeText(this@ManageTeacherActivity, "Échec de la suppression. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
