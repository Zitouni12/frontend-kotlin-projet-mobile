package com.example.facedetectionapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.api.RetrofitClient
import com.example.facedetectionapp.models.Student
import com.example.facedetectionapp.utils.FileUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ManageStudentsActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var rvStudents: RecyclerView
    private lateinit var btnAddStudent: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_students)
        Log.d("ManageStudentsActivity", "onCreate called")

        // Vérifiez les permissions
        checkPermissions()

        // Initialize views
        rvStudents = findViewById(R.id.rvStudents)
        btnAddStudent = findViewById(R.id.btnAddStudent)

        // Set up RecyclerView with Adapter and LayoutManager
        rvStudents.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter()
        rvStudents.adapter = studentAdapter

        // Load students from API
        loadStudents()

        // Button to add a new student
        btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddEditStudentActivity::class.java)
            startActivity(intent)
        }
    }

    // Vérifiez les permissions de stockage
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        }
    }

    // Gère la réponse de la demande de permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission accordée", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Méthode pour obtenir le chemin réel d'un fichier à partir d'une URI
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf("_data")
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow("_data")
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            path
        } else {
            uri.path // Si le curseur est nul, utilisez directement le chemin du fichier
        }
    }


    // Exemple d'utilisation : Obtenir le chemin d'une URI
    private fun exampleUsage(selectedImageUri: Uri?) {
        val filePath = selectedImageUri?.let { getRealPathFromURI(it) }
        Log.d("ManageStudentsActivity", "File path: $filePath")
    }




    // Load students from the API
    private fun loadStudents() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val students = response.body() ?: emptyList()
                    Log.d("ManageStudentsActivity", "Students loaded: $students")
                    studentAdapter.setStudents(students)
                } else {
                    Log.e("ManageStudentsActivity", "Failed to load students: ${response.errorBody()}")
                    Toast.makeText(this@ManageStudentsActivity, "Failed to load students", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Log.e("ManageStudentsActivity", "Error: ${t.message}")
                Toast.makeText(this@ManageStudentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteStudent(id: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.deleteStudent(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ManageStudentsActivity, "Student deleted successfully", Toast.LENGTH_SHORT).show()
                    loadStudents() // Refresh the list
                } else {
                    Toast.makeText(this@ManageStudentsActivity, "Failed to delete student", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ManageStudentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
