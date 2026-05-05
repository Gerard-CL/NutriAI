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

@Composable
fun GeneratedRecipesScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
                          ) {
    // Escuchamos los datos y el estado de carga
    val generatedRecipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Cuando cambian los ingredientes, le decimos al ViewModel que trabaje
    val currentIngredients by viewModel.currentIngredients.collectAsState()

    Scaffold(containerColor = BackgroundColor) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
              ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = NutriGreen, modifier = Modifier.size(32.dp))
            }

            Text(
                text = "¡Mira lo que puedes cocinar!",
                fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), textAlign = TextAlign.Center
                )

            Text(
                text = "Ingredientes detectados: $currentIngredients",
                fontSize = 14.sp, color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), textAlign = TextAlign.Center
                )

            // Si el ViewModel está cargando, mostramos la rueda. Si ya acabó, mostramos la lista.
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NutriGreen)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                          ) {
                    items(generatedRecipes.size) { index ->
                        GeneratedRecipeCard(
                            recipe = generatedRecipes[index],
                            onClick = { onNavigateToDetail(generatedRecipes[index].title) }
                                           )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
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
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit
                      ) {
    // 1. Obtenemos el "State" (la caja) usando '='
    val recipeState = viewModel.selectedRecipe.collectAsState()

    // 2. Extraemos el contenido real de la caja a una variable local inmutable
    val recipe = recipeState.value

    // 3. Verificamos nulos. Si es null, volvemos atrás.
    if (recipe == null) {
        // En Compose, para navegar por un error al cargar la pantalla,
        // SIEMPRE hay que usar LaunchedEffect para no romper el dibujado.
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return // Cortamos la ejecución aquí para que no siga leyendo el código
    }

    // --- A PARTIR DE ESTA LÍNEA, KOTLIN YA SABE QUE 'recipe' ES SEGURO ---
    // Ya te dejará acceder a recipe.title, recipe.calories, etc.

    var personas by rememberSaveable { mutableStateOf(1) }

    Scaffold(containerColor = BackgroundColor) { padding ->
        LazyColumn(
// ... EL RESTO DE TU CÓDIGO SE QUEDA EXACTAMENTE IGUAL ...
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

