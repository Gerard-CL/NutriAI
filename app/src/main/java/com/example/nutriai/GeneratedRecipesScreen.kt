package com.example.nutriai

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriai.BackgroundColor
import com.example.nutriai.NutriGreen


// Este es el "molde" que le dice a la app qué datos tiene una receta
// --- PEGAR AQUÍ, DEBAJO DE LOS IMPORTS ---

// 1. El molde de los ingredientes
data class IngredientData(
    val name: String,
    val baseAmount: Int,
    val unit: String
                         )

// 2. El molde de la receta (REEMPLAZA AL ANTIGUO)
data class RecipeData(
    val title: String,
    val calories: String,
    val time: String,
    val protein: String,
    val carbs: String,
    val ingredients: List<IngredientData>,
    val steps: List<String>
                     )

// 3. Nuestro almacén temporal
object RecipeState {
    var currentRecipes: List<RecipeData> = emptyList()
}

// 1. Añadimos el parámetro de ingredientes a la pantalla
@Composable
fun GeneratedRecipesScreen(
    ingredientsString: String,
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
                          ) {
    // 2. Simulamos la IA de Gemini analizando los ingredientes
    val generatedRecipes by viewModel.recipes.collectAsState()

    LaunchedEffect(key1 = ingredientsString) {
        viewModel.generateRecipes(ingredientsString)
    }

    Scaffold(
        containerColor = BackgroundColor
            ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
              ) {
            // Flecha de retroceso (igual que antes)
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = NutriGreen, modifier = Modifier.size(32.dp))
            }

            Text(
                text = "¡Mira lo que puedes cocinar!",
                fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), textAlign = TextAlign.Center
                )

            // Texto dinámico para ver qué ha entendido la "IA"
            Text(
                text = "Ingredientes detectados: $ingredientsString",
                fontSize = 14.sp, color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), textAlign = TextAlign.Center
                )

            // Lista dinámica
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
                      ) {
                items(generatedRecipes.size) { index ->
                    // Reutilizamos la tarjeta que ya tenías creada
                    GeneratedRecipeCard(recipe = generatedRecipes[index],
                                        onClick = {onNavigateToDetail(generatedRecipes[index].title)})
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

// 3. LA MAGIA: Esta función es nuestro "Fake JSON / Fake API"
// --- REEMPLAZA TU ANTIGUA FUNCIÓN POR ESTA ---

fun simulateGeminiAI(ingredientsText: String): List<RecipeData> {
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

    // Guardamos el resultado en el almacén
    RecipeState.currentRecipes = result

    return result
}

@Composable
fun GeneratedRecipeCard(recipe: RecipeData,
                        onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onClick()},
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
           ) {
            // Placeholder para la imagen de la receta (Cambiar por Image cuando tengas las fotos)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
               )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                    )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila de Calorías y Tiempo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥 ${recipe.calories}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "⏱️ ${recipe.time}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fila de Macros (Proteína y Carbohidratos)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🥩 ${recipe.protein}", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "🥖 ${recipe.carbs}", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
// --- REEMPLAZA TU ANTIGUO RecipeDetailScreen POR ESTE ---

@Composable
fun RecipeDetailScreen(
    recipeTitle: String,
    viewModel: RecipeViewModel, // <--- Recibimos el ViewModel
    onNavigateBack: () -> Unit
                      ) {
    // 1. Buscamos la receta en la lista que tiene el ViewModel guardada
    val recipes by viewModel.recipes.collectAsState()
    val recipe = recipes.find { it.title == recipeTitle }

    if (recipe == null) {
        onNavigateBack()
        return
    }

    // 2. Cambiamos remember por rememberSaveable para que no se resetee al girar la pantalla
    var personas by rememberSaveable { mutableStateOf(1) }

    Scaffold(containerColor = BackgroundColor) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
                  ) {
            item {
                IconButton(onClick = onNavigateBack, modifier = Modifier.padding(vertical = 8.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = NutriGreen, modifier = Modifier.size(32.dp))
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                    ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.LightGray))

                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = recipe.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥 ${recipe.calories}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("🥩 ${recipe.protein}", fontSize = 10.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("🥖 ${recipe.carbs}", fontSize = 10.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                               ) {
                                Text("Personas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { if (personas > 1) personas-- }) {
                                        Icon(Icons.Outlined.RemoveCircleOutline, contentDescription = "Menos", modifier = Modifier.size(28.dp))
                                    }
                                    Text(personas.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                                    IconButton(onClick = { personas++ }) {
                                        Icon(Icons.Default.AddCircle, contentDescription = "Más", modifier = Modifier.size(28.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Ingredientes", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))

                            recipe.ingredients.forEach { ingredient ->
                                val totalAmount = ingredient.baseAmount * personas
                                val displayUnit = if (ingredient.unit == "unidades") "" else ingredient.unit
                                IngredientRow(ingredient.name, "$totalAmount $displayUnit")
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Receta", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(recipe.steps.size) { index ->
                StepCard("Paso ${index + 1}", recipe.steps[index])
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
// Componente para dibujar cada fila de ingredientes
@Composable
fun IngredientRow(name: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
       ) {
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(amount, fontSize = 14.sp, color = Color.DarkGray)
    }
}

// Componente para dibujar cada paso de la receta
@Composable
fun StepCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
        }
    }
}

