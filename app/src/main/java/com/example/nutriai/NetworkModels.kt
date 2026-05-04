package com.example.nutriai

// Esto es lo que Android le enviará a Spring Boot
data class RecipeRequest(
    val ingredients: String
                        )