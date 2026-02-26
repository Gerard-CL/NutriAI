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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
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

// 1. Modificamos la clase para que acepte un número entero (Int) que representa el R.drawable
data class Product(
    val id: Int,
    val name: String,
    val imageRes: Int, // <--- Aquí guardaremos el R.drawable.icono
    var isSelected: Boolean = false
                  )

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CreateRecipeScreen(onNavigateBack: () -> Unit) {

    // 2. Lista de productos.
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
        Product(24, "Galletas", R.drawable.galleta)
                                )

    var products by remember { mutableStateOf(initialProducts) }
    var searchQuery by remember { mutableStateOf("") }
    val selectedCount = products.count { it.isSelected }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Column(modifier = Modifier.background(BackgroundColor)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                   ) {
                    Icon(Icons.Default.Face, contentDescription = "Logo", tint = NutriGreen, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NutriAI", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

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
            Button(
                onClick = { /* Acción para ir a recetas */ },
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
                            ) {
                val filteredProducts = products.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                items(filteredProducts, key = { it.id }) { product ->
                    ProductItem(
                        product = product,
                        modifier = Modifier.animateItem() // Animación de movimiento
                               ) {
                        val newList = products.toMutableList()
                        newList.remove(product)
                        val updatedProduct = product.copy(isSelected = !product.isSelected)

                        if (updatedProduct.isSelected) {
                            newList.add(0, updatedProduct)
                        } else {
                            // Se coloca justo después de los que están seleccionados
                            val currentSelectedCount = newList.count { it.isSelected }
                            newList.add(currentSelectedCount, updatedProduct)
                        }

                        products = newList
                    }
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
            // 3. Usamos Icon con painterResource para tintar tu imagen PNG/SVG automáticamente
            Icon(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                tint = contentColor, // Cambia de negro a blanco
                modifier = Modifier.size(40.dp)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                color = contentColor, // Cambia de negro a blanco
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
                )
        }
    }
}