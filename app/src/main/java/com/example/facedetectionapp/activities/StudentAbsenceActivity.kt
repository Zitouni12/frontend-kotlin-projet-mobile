package com.example.facedetectionapp.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetectionapp.R
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.models.Absence
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent


class StudentAbsencesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var absenceAdapter: AbsenceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_absences)

        recyclerView = findViewById(R.id.recyclerViewAbsences)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        token?.let {
            fetchStudentAbsences(it)
        } ?: run {
            Toast.makeText(this, "Token manquant, veuillez vous reconnecter.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchStudentAbsences(token: String) {
        val apiService = ApiService.create(this)

        apiService.getStudentAbsences("Bearer $token")?.enqueue(object : Callback<List<Absence>> {
            override fun onResponse(call: Call<List<Absence>>, response: Response<List<Absence>>) {
                if (response.isSuccessful) {
                    val absences = response.body()
                    if (absences != null) {
                        absenceAdapter = AbsenceAdapter(absences)
                        recyclerView.adapter = absenceAdapter
                    }
                } else {
                    Toast.makeText(this@StudentAbsencesActivity, "Erreur lors du chargement des absences", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Absence>>, t: Throwable) {
                Toast.makeText(this@StudentAbsencesActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
