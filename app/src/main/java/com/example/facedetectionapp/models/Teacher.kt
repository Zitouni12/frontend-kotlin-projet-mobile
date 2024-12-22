package com.example.facedetectionapp.models

import com.google.gson.annotations.SerializedName

data class Teacher(
    val id: Int,

    val nom: String,
    val prenom: String,
    @SerializedName("date_embauche") val dateEmbauche: String?,

    val matiere: String?,
    val filiere: String?
)