package com.example.nutriai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    // ---------------------------------------------------------
    // BLOQUE 1: LÓGICA DE RECETAS Y MOCK API (Tu código original)
    // ---------------------------------------------------------
    // Ponlo en 'true' para usar datos falsos y diseñar.
    // Ponlo en 'false' cuando arranques tu Spring Boot.
    private val USE_MOCK_API = false

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
        Product(26, "Sal", R.drawable.sal)
                                )

    private val _basicosProducts = MutableStateFlow(initialProducts)
    val basicosProducts: StateFlow<List<Product>> = _basicosProducts.asStateFlow()

    fun toggleBasicoSelection(productId: Int) {
        _basicosProducts.value = _basicosProducts.value.map { product ->
            if (product.id == productId) {
                product.copy(isSelected = !product.isSelected)
            } else {
                product
            }
        }
    }

    fun setIngredientsAndSearch(ingredients: String) {
        // 1. Guardamos los ingredientes cortos ("Huevos, Patatas") solo para que la UI los muestre
        _currentIngredients.value = ingredients

        // 2. Construimos el Super-Prompt con todo el contexto
        val fullPromptForAI = buildAIPrompt(ingredients)

        // 3. Imprimimos en la consola de Android Studio para que tú veas qué se envía realmente
        println("=== ENVIANDO A LA IA ===")
        println(fullPromptForAI)
        println("========================")

        // 4. Se lo mandamos a Retrofit (o al simulador)
        generateRecipes(fullPromptForAI)
    }

    fun selectRecipe(title: String) {
        val foundRecipe = _recipes.value.find { it.title == title }
        _selectedRecipe.value = foundRecipe
    }

    fun generateRecipes(ingredientsText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
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

    // --- FUNCIÓN AUXILIAR: Traduce los [1, 5, 8] a "Bacon, Pan, Pollo" ---
    private fun getFoodNamesFromIds(ids: Set<Int>): String {
        if (ids.isEmpty()) return "Ninguno"
        // Filtramos la lista de productos iniciales buscando los que coinciden con los IDs
        return initialProducts
            .filter { product -> ids.contains(product.id) }
            .joinToString(", ") { product -> product.name }
    }

    // --- EL CONSTRUCTOR DEL SUPER-PROMPT ---
    private fun buildAIPrompt(baseIngredients: String): String {
        val dislikes = getFoodNamesFromIds(_dislikedFoodIds.value)
        val staples = getFoodNamesFromIds(_stapleFoodIds.value)
        val restrictions = if (_userRestrictions.value.isEmpty() || _userRestrictions.value.contains("Ninguna")) {
            "Ninguna"
        } else {
            _userRestrictions.value.joinToString(", ")
        }

        // Construimos el texto exacto que leerá la IA
        return """
            Actúa como un chef nutricionista experto. Crea recetas utilizando principalmente estos ingredientes: $baseIngredients.
            
            Es VITAL que respetes este contexto del usuario para generar la receta:
            - Raciones a cocinar: ${_userPortions.value.ifEmpty { "1 persona" }}
            - Nivel de experiencia en cocina: ${_userLevel.value.ifEmpty { "Intermedio" }}
            - Tiempo máximo de preparación: ${_userTime.value.ifEmpty { "Sin límite" }}
            - Restricciones dietéticas o alergias: $restrictions
            - ALIMENTOS PROHIBIDOS (NO incluyas esto bajo ningún concepto): $dislikes
            - Alimentos de la despensa del usuario que puedes usar libremente como apoyo: $staples
            
            Instrucciones extra: Ajusta las cantidades matemáticas de los ingredientes basándote estrictamente en el número de raciones.
        """.trimIndent()
    }

    // ---------------------------------------------------------
    // BLOQUE 2: NUEVA MEMORIA GLOBAL (Onboarding y Configuración)
    // ---------------------------------------------------------

    private val _userGender = MutableStateFlow("")
    val userGender = _userGender.asStateFlow()

    private val _userPortions = MutableStateFlow("")
    val userPortions = _userPortions.asStateFlow()

    private val _userLevel = MutableStateFlow("")
    val userLevel = _userLevel.asStateFlow()

    private val _userTime = MutableStateFlow("")
    val userTime = _userTime.asStateFlow()

    private val _userRestrictions = MutableStateFlow<Set<String>>(emptySet())
    val userRestrictions = _userRestrictions.asStateFlow()

    private val _dislikedFoodIds = MutableStateFlow<Set<Int>>(emptySet())
    val dislikedFoodIds = _dislikedFoodIds.asStateFlow()

    private val _stapleFoodIds = MutableStateFlow<Set<Int>>(emptySet())
    val stapleFoodIds = _stapleFoodIds.asStateFlow()

    // Funciones para actualizar la memoria global
    fun setGender(gender: String) { _userGender.value = gender }
    fun setPortions(portions: String) { _userPortions.value = portions }
    fun setLevel(level: String) { _userLevel.value = level }
    fun setTime(time: String) { _userTime.value = time }
    fun setRestrictions(restrictions: Set<String>) { _userRestrictions.value = restrictions }
    fun setDislikedFoods(foods: Set<Int>) { _dislikedFoodIds.value = foods }
    fun setStapleFoods(foods: Set<Int>) { _stapleFoodIds.value = foods }
}