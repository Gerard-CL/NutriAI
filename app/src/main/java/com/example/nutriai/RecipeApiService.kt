package com.example.nutriai

import retrofit2.http.Body
import retrofit2.http.POST

interface RecipeApiService {

    // Le decimos que haga una petición POST a la ruta de tu futuro Spring Boot
    // La palabra "suspend" es clave: indica que esta función se ejecutará en una corrutina (segundo plano)
    @POST("api/recetas/generar")
    suspend fun generateRecipes(@Body request: RecipeRequest): List<RecipeData>
}