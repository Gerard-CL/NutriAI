package com.example.nutriai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeData>>(emptyList())
    val recipes: StateFlow<List<RecipeData>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun generateRecipes(ingredientsText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
                // 1. Preparamos el paquete que vamos a enviar
                val requestBody = RecipeRequest(ingredientsText)

                // 2. Hacemos la llamada al servidor Spring Boot
                // La app se "pausará" aquí (en segundo plano) hasta que el servidor responda
                val response = RetrofitClient.instance.generateRecipes(requestBody)

                // 3. ¡Actualizamos la pantalla con las recetas del servidor!
                _recipes.value = response

            } catch (e: Exception) {
                // Si el servidor está apagado o hay un error, capturamos el fallo
                e.printStackTrace()
                // Opcional: Podrías cargar unas recetas "de prueba" aquí para que la app no quede vacía
                // mientras desarrollas el backend.
            } finally {
                _isLoading.value = false
            }
        }
    }
}