package com.example.nutriai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    // --- EL INTERRUPTOR MÁGICO ---
    // Ponlo en 'true' para usar datos falsos y diseñar.
    // Ponlo en 'false' cuando arranques tu Spring Boot.
    private val USE_MOCK_API = true

    private val _recipes = MutableStateFlow<List<RecipeData>>(emptyList())
    val recipes: StateFlow<List<RecipeData>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentIngredients = MutableStateFlow("Nada")
    val currentIngredients: StateFlow<String> = _currentIngredients.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<RecipeData?>(null)
    val selectedRecipe: StateFlow<RecipeData?> = _selectedRecipe.asStateFlow()

    val initialProducts = listOf(
        Product(1, "Bacon", R.drawable.bacon),
        Product(2, "Huevos", R.drawable.huevos),
        Product(3, "Patatas", R.drawable.patatas),
        Product(4, "Leche", R.drawable.botella_de_leche),
        Product(5, "Pan", R.drawable.pan),
        Product(6, "Plátanos", R.drawable.platano),
        Product(7, "Cebolla", R.drawable.cebolla),
        Product(8, "Pollo", R.drawable.pollo),
        Product(9, "Queso", R.drawable.queso),
        Product(10, "Yogur", R.drawable.yogur),
        Product(11, "Atún", R.drawable.atun),
        Product(12, "Galletas", R.drawable.galleta),
        Product(13, "Bacon", R.drawable.bacon),
        Product(14, "Huevos", R.drawable.huevos),
        Product(15, "Patatas", R.drawable.patatas),
        Product(16, "Leche", R.drawable.botella_de_leche),
        Product(17, "Pan", R.drawable.pan),
        Product(18, "Plátanos", R.drawable.platano),
        Product(19, "Cebolla", R.drawable.cebolla),
        Product(20, "Pollo", R.drawable.pollo),
        Product(21, "Queso", R.drawable.queso),
        Product(22, "Yogur", R.drawable.yogur),
        Product(23, "Atún", R.drawable.atun),
        Product(24, "Galletas", R.drawable.galleta),
        Product(25, "Aceite", R.drawable.aceite),
        Product(26, "Sal", R.drawable.sal),
                                )

    // 2. Creamos el estado para que la pantalla lo escuche
    private val _basicosProducts = MutableStateFlow(initialProducts)
    val basicosProducts: StateFlow<List<Product>> = _basicosProducts.asStateFlow()

    // 3. Función para marcar/desmarcar un producto
    fun toggleBasicoSelection(productId: Int) {
        _basicosProducts.value = _basicosProducts.value.map { product ->
            if (product.id == productId) {
                product.copy(isSelected = !product.isSelected)
            } else {
                product
            }
        }
    }

    // NUEVO: Función para guardar ingredientes y lanzar la búsqueda a la vez
    fun setIngredientsAndSearch(ingredients: String) {
        _currentIngredients.value = ingredients
        generateRecipes(ingredients)
    }

    // NUEVO: Función para seleccionar la receta antes de ir al detalle
    fun selectRecipe(title: String) {
        val foundRecipe = _recipes.value.find { it.title == title }
        _selectedRecipe.value = foundRecipe
    }

    fun generateRecipes(ingredientsText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
                // Dependiendo del interruptor, usa la red o usa la mentira
                val response = if (USE_MOCK_API) {
                    simulateGeminiAI(ingredientsText)
                } else {
                    RetrofitClient.instance.getRecipes(ingredientsText)
                }

                _recipes.value = response

            } catch (e: Exception) {
                e.printStackTrace()
                _recipes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Tu simulador, guardado aquí a salvo hasta que lo borres al final del proyecto
    private fun simulateGeminiAI(ingredientsText: String): List<RecipeData> {
        val result = mutableListOf<RecipeData>()
        val ingredients = ingredientsText.lowercase()

        if (ingredients.contains("huevos") && ingredients.contains("patatas")) {
            result.add(
                RecipeData(
                    title = "Tortilla de Patata Clásica", calories = "300 Cal", time = "25 min", protein = "15 g", carbs = "30 g",
                    ingredients = listOf(
                        IngredientData("Patata/s", 2, "unidades"),
                        IngredientData("Huevo/s", 3, "unidades"),
                        IngredientData("Cebolla", 50, "g")
                                        ),
                    steps = listOf(
                        "Pela y lava las patatas y la cebolla.",
                        "Fríe las patatas y la cebolla a fuego medio.",
                        "Bate los huevos y mezcla todo.",
                        "Cuaja la tortilla en una sartén con un poco de aceite."
                                  )
                          )
                      )
        }

        if (result.isEmpty()) {
            result.add(
                RecipeData(
                    title = "Huevos Estrellados", calories = "200 Cal", time = "8 min", protein = "20 g", carbs = "80 g",
                    ingredients = listOf(
                        IngredientData("Patata/s", 2, "unidades"),
                        IngredientData("Huevo/s", 2, "unidades"),
                        IngredientData("Jamón", 100, "g")
                                        ),
                    steps = listOf(
                        "Pela, lava y corta las patatas en rodajas finas.",
                        "Fríe las patatas en abundante aceite.",
                        "Fríe los huevos dejando la yema líquida.",
                        "Coloca las patatas, el jamón por encima y los huevos coronando el plato."
                                  )
                          )
                      )
        }
        return result
    }
}