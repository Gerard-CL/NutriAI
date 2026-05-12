package com.example.nutriai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit
                      ) {
    // 1. Obtenemos el estado (la "cámara en directo")
    val recipeState = viewModel.selectedRecipe.collectAsState()

    // 2. Extraemos el valor a una constante inmutable (la "foto fija")
    val recipe = recipeState.value

    // 3. Comprobación segura
    if (recipe == null) {
        // En Compose, para navegar por un error al cargar, usamos LaunchedEffect
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return // Cortamos aquí para que no intente leer el resto
    }

    // --- A PARTIR DE AQUÍ EL CÓDIGO YA SABE QUE 'recipe' ES SEGURO ---

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