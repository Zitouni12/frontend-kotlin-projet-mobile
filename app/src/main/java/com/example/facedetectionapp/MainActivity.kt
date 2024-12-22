package com.example.facedetectionapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.activities.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Références aux LinearLayouts des boutons
        val btnTeacher: LinearLayout = findViewById(R.id.teacher)
        val btnAdmin: LinearLayout = findViewById(R.id.admin)
        val btnStudent: LinearLayout = findViewById(R.id.student)

        // Méthode générique pour lancer LoginActivity
        val startLoginActivity: (LinearLayout) -> Unit = { _ ->
            val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        // Événements de clic
        btnTeacher.setOnClickListener { startLoginActivity(btnTeacher) }
        btnAdmin.setOnClickListener { startLoginActivity(btnAdmin) }
        btnStudent.setOnClickListener { startLoginActivity(btnStudent) }
    }
}