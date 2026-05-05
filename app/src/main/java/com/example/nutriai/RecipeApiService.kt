package com.example.nutriai

import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("api/recipes/{ingredients}")
    suspend fun getRecipes(@Path("ingredients") text: String): List<RecipeData>
}