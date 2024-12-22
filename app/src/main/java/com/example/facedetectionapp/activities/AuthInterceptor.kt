package com.example.facedetectionapp.activities

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Intercepteur pour ajouter le token JWT à chaque requête
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Vérifier si la requête est pour l'API de connexion
        if (request.url.toString().contains("/auth/login/")) {
            // Si c'est l'API de connexion, ne pas ajouter le token
            return chain.proceed(request)
        }

        // Récupérer le token depuis SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        Log.d("SharedPreferences", "Token sauvegardé: $token ")

        // Log pour vérifier les actions de l'intercepteur
        Log.d("Interceptor", "Requête interceptée: ${request.url}")
        if (token != null) {
            Log.d("Interceptor", "Token ajouté: $token")
        } else {
            Log.d("Interceptor", "Aucun token trouvé dans SharedPreferences")
        }

        // Si le token existe, l'ajouter à l'en-tête Authorization
        val modifiedRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(modifiedRequest)
    }
}
    fun provideRetrofit(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))  // Ajouter l'intercepteur
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.106:8000/")  // Remplacer par votre URL de serveur
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


