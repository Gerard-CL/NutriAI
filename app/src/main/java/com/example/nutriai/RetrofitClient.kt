package com.example.nutriai

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANTE: Cuando pruebas en el emulador de Android, "localhost" se refiere al propio móvil.
    // Para acceder al localhost de tu ordenador (donde correrá Spring Boot), debes usar "10.0.2.2".
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val instance: RecipeApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a Kotlin automáticamente
            .build()

        retrofit.create(RecipeApiService::class.java)
    }
}