package com.example.facedetectionapp.api



import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
   private const val BASE_URL = "http://100.70.36.208:8000/" // Remplacez par l'IP de votre backend

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Capture tous les détails des requêtes/réponses
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS) // Timeout de connexion
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)    // Timeout de lecture
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)   // Timeout d'écriture
        .addInterceptor(loggingInterceptor)
        .build()


    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
