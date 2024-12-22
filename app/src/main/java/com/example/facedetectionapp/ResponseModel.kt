package com.example.facedetectionapp

data class FaceDetectionResponse(
    val faces: List<Face>
)

data class Face(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)
