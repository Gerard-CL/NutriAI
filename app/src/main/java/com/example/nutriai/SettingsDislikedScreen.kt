package com.example.nutriai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsDislikedScreen(viewModel: RecipeViewModel, onNavigateBack: () -> Unit) {
    val savedIds by viewModel.dislikedFoodIds.collectAsState()
    var tempSelection by remember(savedIds) { mutableStateOf(savedIds) }
    var searchQuery by remember { mutableStateOf("") }

    val allProducts = viewModel.initialProducts.take(12)

    // --- CAMBIO CLAVE AQUÍ ---
    // Primero filtramos por la búsqueda, y luego ORDENAMOS para que los seleccionados vayan primero.
    val filteredProducts = allProducts
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .sortedByDescending { tempSelection.contains(it.id) } // Si es 'true' (está seleccionado), va arriba del todo

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = NutriGreen)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Button(
                    onClick = {
                        viewModel.setDislikedFoods(tempSelection) // Guarda en el cerebro
                        onNavigateBack() // Vuelve atrás
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutriGreen)
                      ) {
                    Text("Guardar", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
            ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            Text("Alimentos que no te gustan", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextColor)
            Spacer(modifier = Modifier.height(16.dp))

            SearchBarOnboarding(searchQuery) { searchQuery = it }

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                // Es vital mantener el 'key = { it.id }' para que Compose sepa qué casilla se está moviendo
                items(filteredProducts, key = { it.id }) { product ->
                    val isSelected = tempSelection.contains(product.id)
                    ProductItem(
                        product = product.copy(isSelected = isSelected),
                        selectedColor = NutriGreen,
                        modifier = Modifier.animateItem(), // --- SEGUNDO CAMBIO: Animación de movimiento fluida ---
                        onClick = {
                            tempSelection = if (isSelected) tempSelection - product.id else tempSelection + product.id
                        }
                               )
                }
            }
        }
    }
}