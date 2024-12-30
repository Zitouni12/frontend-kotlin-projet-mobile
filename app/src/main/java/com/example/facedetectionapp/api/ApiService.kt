package com.example.facedetectionapp.api
import com.example.facedetectionapp.FaceDetectionResponse
import com.example.facedetectionapp.models.LoginRequest
import com.example.facedetectionapp.models.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.GET

import com.example.facedetectionapp.models.Teacher
import com.example.facedetectionapp.models.Student
import retrofit2.http.Body
import android.content.Context
import com.example.facedetectionapp.models.Absence
import com.example.facedetectionapp.models.StudentResponse
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @Multipart
    @POST("/detect-faces/")
    fun detectFaces(@Part image: MultipartBody.Part): Call<FaceDetectionResponse>


    //login

    @POST("auth/login/")  // Appel de l'API de connexion sans token
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>


    //enseignants
    @GET("api/enseignants/")
    fun getTeachers(): Call<List<Teacher>>

    @POST("api/enseignants/create/")
    fun addTeacher(@Body teacher: Teacher): Call<Teacher>

    @GET("api/enseignants/{id}/")
    fun getTeacherById(@Path("id") teacherId: Int): Call<Teacher>

    @PUT("api/enseignants/{id}/")
    fun updateTeacher(@Path("id") teacherId: Int, @Body teacher: Teacher): Call<Teacher>

    @DELETE("api/enseignants/{id}/")
    fun deleteTeacher(@Path("id") teacherId: Int): Call<Void>

    //Student

    @GET("student/")
    fun getStudents(): Call<List<Student>>

    @Multipart
    @POST("student/")
    fun addStudent(
        @Part("student_id") studentId: RequestBody,  // Changer "studentId" en "student_id"
        @Part("first_name") firstName: RequestBody,  // Changer "firstName" en "first_name"
        @Part("last_name") lastName: RequestBody,    // Changer "lastName" en "last_name"
        @Part("birth_date") birthDate: RequestBody,  // Changer "birthDate" en "birth_date"
        @Part("status") status: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<StudentResponse>

    @GET("student/{id}/")
    fun getStudentById(@Path("id") id: Int): Call<Student>


    @DELETE("student/{id}/")
    fun deleteStudent(@Path("id") id: Int): Call<Void>

    @Multipart
    @PUT("student/{id}/")
    fun updateStudent(
        @Path("id") id: Int,  // Spécifie l'ID de l'étudiant à mettre à jour
        @Part("student_id") studentId: RequestBody,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("birth_date") birthDate: RequestBody,
        @Part("status") status: RequestBody,
        @Part image: MultipartBody.Part?  // Optionnel, seulement si l'image est modifiée
    ): Call<StudentResponse>


    // Récupérer le profil de l'étudiant
    @GET("student/student/profile/")
    fun getStudentProfile(
        @Header("Authorization") authorization: String
    ): Call<Student>

    // Récupérer les absences de l'étudiant
    @GET("student/student/absences/")
    fun getStudentAbsences(
        @Header("Authorization") authorization: String
    ): Call<List<Absence>>




    companion object {
        fun create(context: Context): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.106:8000/")  // Remplacez par votre URL API
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }





}


