package com.example.facedetectionapp.models
import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id") val id: Int,
    @SerializedName("student_id") val studentId: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("image") val image: String
)
