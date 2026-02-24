package com.example.nutriai

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Estructura de datos para nuestros productos
data class Product(
    val id: Int,
    val name: String,
    val icon: ImageVector, // En el futuro lo cambiarás por un Int (R.drawable...)
    var isSelected: Boolean = false
                  )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(onNavigateBack: () -> Unit) {
    // Lista inicial de productos
    val initialProducts = listOf(
        Product(1, "Bacon", Icons.Default.Menu), // Usa iconos reales luego
        Product(2, "Huevos", Icons.Default.Face),
        Product(3, "Patatas", Icons.Default.Info),
        Product(4, "Leche", Icons.Default.Delete),
        Product(5, "Pan", Icons.Default.Home),
        Product(6, "Plátanos", Icons.Default.ShoppingCart),
        Product(7, "Cebolla", Icons.Default.AccountCircle),
        Product(8, "Pollo", Icons.Default.Build),
        Product(9, "Queso", Icons.Default.Warning),
        Product(10, "Yogur", Icons.Default.ThumbUp),
        Product(11, "Atún", Icons.Default.MailOutline),
        Product(12, "Galletas", Icons.Default.Star)
                                )

    // Estado: Aquí guardamos la lista y el texto del buscador
    var products by remember { mutableStateOf(initialProducts) }
    var searchQuery by remember { mutableStateOf("") }

    // Contamos cuántos hay seleccionados
    val selectedCount = products.count { it.isSelected }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Column(modifier = Modifier.background(BackgroundColor)) {
                // Header superior (Logo NutriAI)
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                   ) {
                    Icon(Icons.Default.Face, contentDescription = "Logo", tint = NutriGreen, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NutriAI", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                // Header secundario (Flecha y Título)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                   ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", modifier = Modifier.size(28.dp))
                    }
                    Text(
                        text = "Mis Productos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                        )
                }
            }
        },
        bottomBar = {
            // Botón verde inferior
            Button(
                onClick = { /* Ir a ver recetas con los productos seleccionados */ },
                colors = ButtonDefaults.buttonColors(containerColor = NutriGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
                  ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ver Recetas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("($selectedCount Seleccionados)", fontSize = 12.sp)
                }
            }
        }
            ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
              ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = NutriGreen
                                                         ),
                trailingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
                             )

            Spacer(modifier = Modifier.height(20.dp))

            // Cuadrícula de productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
                            ) {
                // Filtramos por búsqueda
                val filteredProducts = products.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                items(filteredProducts, key = { it.id }) { product ->
                    ProductItem(
                        product = product,
                        // ¡ESTA ES LA LÍNEA MÁGICA!
                        modifier = Modifier.animateItem(), // Si te sale en rojo, prueba con: Modifier.animateItemPlacement()
                        onClick = {
                            // ... Aquí dentro dejas exactamente el mismo código
                            // de val newList = ..., newList.remove..., etc. que ya tenías ...

                            val newList = products.toMutableList()
                            newList.remove(product)

                            val updatedProduct = product.copy(isSelected = !product.isSelected)

                            if (updatedProduct.isSelected) {
                                newList.add(0, updatedProduct)
                            } else {
                                newList.add(updatedProduct)
                            }

                            products = newList
                        }
                               )
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bgColor = if (product.isSelected) NutriGreen else Color.White
    val contentColor = if (product.isSelected) Color.White else Color.Black
    val borderColor = if (product.isSelected) NutriGreen else Color(0xFFEEEEEE)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if(product.isSelected) 4.dp else 2.dp),
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() }
        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
              ) {
            Icon(
                imageVector = product.icon,
                contentDescription = product.name,
                tint = contentColor,
                modifier = Modifier.size(40.dp)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
                )
        }
    }
}