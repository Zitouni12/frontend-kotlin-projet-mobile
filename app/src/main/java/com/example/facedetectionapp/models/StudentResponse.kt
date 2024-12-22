package com.example.facedetectionapp.models

data class StudentResponse(
    val id: Int,
    val student_id: String,
    val first_name: String,
    val last_name: String,
    val birth_date: String,
    val status: String,
    val image: String
)
