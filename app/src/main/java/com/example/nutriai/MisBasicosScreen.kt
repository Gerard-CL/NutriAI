package com.example.nutriai

import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.HorizontalDivider
// ... y asegúrate de tener el resto del código que te pasé pegado más abajo



@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MisBasicosScreen(
    viewModel: RecipeViewModel,
    onNavigateToInicio: () -> Unit = {},
    onNavigateToConfig: () -> Unit = {}
                    ) {
    // Lista inicial de productos (Añade aquí los tuyos)
    // He puesto los 3 primeros en true para que aparezcan arriba por defecto como en tu foto
    val products by viewModel.basicosProducts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val misBasicos = filteredProducts.filter { it.isSelected }
    val masProductos = filteredProducts.filter { !it.isSelected }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            Column {
                // --- NUEVO: BOTÓN DE GUARDAR CONDICIONAL ---
                // Si la lista de básicos tiene al menos 1 elemento, el botón aparece
                AnimatedVisibility(
                    visible = misBasicos.isNotEmpty()
                                  ) {
                    Button(
                        onClick = {
                            // TODO: Aquí pones la lógica para guardar los datos
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NutriGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp)
                          ) {
                        Text(
                            text = "Guardar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                            )
                    }
                }
                // --- FIN DEL NUEVO BOTÓN ---

                // Buscador en la parte inferior como en tu diseño
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = NutriGreen
                                                                 ),
                        trailingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
                        modifier = Modifier.fillMaxWidth()
                                     )
                }
                HorizontalDivider(color = Color.LightGray)
                BottomNavigationBar(
                    currentRoute = "mis_basicos",
                    onInicioClick = onNavigateToInicio,
                    onBasicosClick = { /* Ya estamos en básicos */ },
                    onConfigClick = onNavigateToConfig
                                   )
            }
        }
            ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 3 columnas
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                        ) {
            // --- CABECERA PRINCIPAL ---
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                       ) {
                        Text(
                            text = "Mis Básicos",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                            ) // Para centrar el texto compensando el botón
                    }
                    Text(
                        text = "Que es lo que siempre sueles tener?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 16.dp)
                        )
                }
            }

            // --- SECCIÓN ARRIBA: MIS BÁSICOS (VERDES) ---
            items(misBasicos, key = { it.id }) { product ->
                ProductItem(
                    product = product,
                    modifier = Modifier.animateItem() // ¡Esto hace la animación suave al cambiar de lista!
                           ) {
                    // LLAMAMOS AL VIEWMODEL
                    viewModel.toggleBasicoSelection(product.id)
                }
            }

            // --- CABECERA DE LA SEGUNDA SECCIÓN ---
            // Solo se muestra si hay productos sin seleccionar
            if (masProductos.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Más productos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 8.dp)
                        )
                }
            }

            // --- SECCIÓN ABAJO: MÁS PRODUCTOS (BLANCOS) ---
            items(masProductos, key = { it.id }) { product ->
                ProductItem(
                    product = product,
                    modifier = Modifier.animateItem()
                           ) {
                    // LLAMAMOS AL VIEWMODEL
                    viewModel.toggleBasicoSelection(product.id)
                }
            }
        }
    }
}

@Composable
fun TopLogoSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
       ) {
        // Reemplaza esto con tu logo real: painterResource(id = R.drawable.tu_logo)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(NutriGreen),
            contentAlignment = Alignment.Center
           ) {
            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Logo", tint = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "NutriAI",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
            )
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
       ) {
        IconButton(
            onClick = { /* Acción de volver */ },
            modifier = Modifier.align(Alignment.CenterStart)
                  ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
                )
        }
        Text(
            text = "Mis Bàsicos",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
            )
    }
}

@Composable
fun ItemsGrid(isBasic: Boolean) {
    // Datos de ejemplo para simular tu diseño
    val items = if (isBasic) {
        listOf("Aceite", "Huevos", "Sal", "Aceite", "Huevos", "Sal")
    } else {
        listOf("Leche", "Pan", "Platanos", "Leche", "Pan", "Platanos")
    }

    // Dividimos la lista en grupos de 3 para crear las filas
    val chunkedItems = items.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        chunkedItems.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
               ) {
                rowItems.forEach { itemName ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(name = itemName, isBasic = isBasic)
                    }
                }
                // Relleno para que la última fila mantenga el tamaño si tiene menos de 3 elementos
                val emptySpots = 3 - rowItems.size
                repeat(emptySpots) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ProductCard(name: String, isBasic: Boolean) {
    val backgroundColor = if (isBasic) NutriGreen else Color.White
    val textColor = if (isBasic) Color.White else Color.DarkGray
    val borderStroke = if (isBasic) null else BorderStroke(1.dp, Color.LightGray)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f) // Mantiene la forma casi cuadrada
        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
              ) {
            // AQUÍ: Usa painterResource con tus imágenes. Ejemplo: painterResource(id = R.drawable.aceite)
            Icon(
                imageVector = Icons.Outlined.ShoppingCart, // Placeholder temporal
                contentDescription = name,
                tint = if (isBasic) Color.White else Color.DarkGray,
                modifier = Modifier.size(40.dp)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
                )
        }
    }
}

@Composable
fun SearchBarBottom() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 12.dp)
       ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp), // Bordes muy redondeados
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = NutriGreen
                                                     ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.DarkGray
                    )
            }
                         )
    }
}

