package com.example.nutriai

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    onNavigateBack: () -> Unit
                    ) {
    val totalPages = 7
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()

    // ---------------------------------------------------------
    // LA MEMORIA DEL CUESTIONARIO
    // ---------------------------------------------------------
    var selectedGender by remember { mutableStateOf("") }
    var selectedPortions by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedRestrictions by remember { mutableStateOf(setOf<String>()) }

    // IMPORTANTE: Ahora guardamos un Set de enteros (IDs) en lugar de Strings
    var dislikedFoodIds by remember { mutableStateOf(setOf<Int>()) }
    var stapleFoodIds by remember { mutableStateOf(setOf<Int>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Limpiamos el buscador al cambiar de página
    LaunchedEffect(pagerState.currentPage) {
        searchQuery = ""
    }

    // LISTA MAESTRA DE ALIMENTOS (Usando tu data class Product)
    // Extraemos los 12 productos únicos de tu código para el cuestionario
    val allProducts = listOf(
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
        Product(12, "Galletas", R.drawable.galleta)
                            )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    } else {
                        onNavigateBack()
                    }
                },
                modifier = Modifier.padding(8.dp)
                      ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Button(
                    onClick = {
                        if (pagerState.currentPage < totalPages - 1) {
                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            println("Datos finales guardados. No gustan IDs: $dislikedFoodIds, Básicos IDs: $stapleFoodIds")
                            onFinishOnboarding()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TextColor)
                      ) {
                    Text("Continuar", fontSize = 16.sp, color = Color.White)
                }
            }
        }
            ) { paddingValues ->

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            userScrollEnabled = false
                       ) { page ->
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {

                // Títulos (sin cambios)
                val title = when(page) {
                    0 -> "Elige tu género"
                    1 -> "¿Para cuántos cocinas?"
                    2 -> "¿Cuál es tu nivel en la cocina?"
                    3 -> "¿Cuánto tiempo sueles tener?"
                    4 -> "¿Tienes alguna restricción?"
                    5 -> "¿Qué alimentos no te gustan?"
                    6 -> "¿Qué alimentos tienes siempre en casa?"
                    else -> ""
                }

                val subtitle = when(page) {
                    0 -> "Esto se usará para calibrar tu plan personalizado."
                    1 -> "Ajustaremos las cantidades de los ingredientes a tus necesidades."
                    2 -> "Te sugeriremos recetas adaptadas a tus habilidades."
                    3 -> "Filtraremos las opciones para adaptarnos a tu ritmo diario."
                    4 -> "Puedes seleccionar varias opciones para crear un filtro seguro."
                    5 -> "Los excluiremos automáticamente de todas tus sugerencias de recetas."
                    6 -> "Los usaremos como base para recomendarte recetas más accesibles."
                    else -> ""
                }

                Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextColor, lineHeight = 34.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(32.dp))

                when (page) {
                    // Páginas 1 a 4 (Idénticas a antes, las omito para no saturar pero NO LAS BORRES de tu código)
                    0 -> {
                        val options = listOf("Hombre", "Mujer", "Otro")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            options.forEach { option ->
                                OnboardingOptionButton(option, selectedGender == option) { selectedGender = option }
                            }
                        }
                    }
                    1 -> {
                        val options = listOf("Solo para mi", "Para 2 personas", "3 personas", "4 personas", "Familias grandes (5+)")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            options.forEach { option ->
                                OnboardingOptionButton(option, selectedPortions == option) { selectedPortions = option }
                            }
                        }
                    }
                    2 -> {
                        val options = listOf("Principiante", "Intermedio", "Experto")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            options.forEach { option ->
                                OnboardingOptionButton(option, selectedLevel == option) { selectedLevel = option }
                            }
                        }
                    }
                    3 -> {
                        val options = listOf("Menos de 15 minutos", "De 15 a 30 minutos", "De 30 a 60 minutos", "Más de una hora")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            options.forEach { option ->
                                OnboardingOptionButton(option, selectedTime == option) { selectedTime = option }
                            }
                        }
                    }
                    4 -> {
                        val options = listOf("Ninguna", "Vegetariano", "Vegano", "Sin gluten", "Sin lactosa")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            options.forEach { option ->
                                val isSelected = selectedRestrictions.contains(option)
                                OnboardingOptionButton(option, isSelected) {
                                    if (option == "Ninguna") selectedRestrictions = setOf("Ninguna")
                                    else selectedRestrictions = if (isSelected) selectedRestrictions - option else selectedRestrictions - "Ninguna" + option
                                }
                            }
                        }
                    }

                    5 -> {
                        val filteredProducts = allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }

                        Column(modifier = Modifier.fillMaxSize()) {
                            SearchBarOnboarding(searchQuery) { searchQuery = it }
                            Spacer(modifier = Modifier.height(24.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                                            ) {
                                items(filteredProducts, key = { it.id }) { product ->
                                    // Comprobamos si el ID de este producto está en la lista de "no me gustan"
                                    val isSelected = dislikedFoodIds.contains(product.id)
                                    // Le pasamos una "copia" del producto obligando al isSelected a ser el correcto
                                    val displayProduct = product.copy(isSelected = isSelected)

                                    // ¡AQUÍ ESTÁ LA MAGIA! Llamamos a TU función
                                    ProductItem(
                                        product = displayProduct,
                                        selectedColor = TextColor,
                                        onClick = {
                                            dislikedFoodIds = if (isSelected) dislikedFoodIds - product.id else dislikedFoodIds + product.id
                                        }
                                               )
                                }
                            }
                        }
                    }

                    6 -> {
                        val filteredProducts = allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }

                        Column(modifier = Modifier.fillMaxSize()) {
                            SearchBarOnboarding(searchQuery) { searchQuery = it }
                            Spacer(modifier = Modifier.height(24.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                                            ) {
                                items(filteredProducts, key = { it.id }) { product ->
                                    val isSelected = stapleFoodIds.contains(product.id)
                                    val displayProduct = product.copy(isSelected = isSelected)

                                    // ¡Volvemos a llamar a TU función!
                                    ProductItem(
                                        product = displayProduct,
                                        selectedColor = TextColor,
                                        onClick = {
                                            stapleFoodIds = if (isSelected) stapleFoodIds - product.id else stapleFoodIds + product.id
                                        }
                                               )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------
// COMPONENTES REUTILIZABLES (Botones de Texto y Buscador)
// ---------------------------------------------------------

@Composable
fun OnboardingOptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) TextColor else BackgroundColor,
            contentColor = if (isSelected) Color.White else TextColor
                                            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if(isSelected) 4.dp else 0.dp)
          ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
fun SearchBarOnboarding(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar alimento...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        shape = RoundedCornerShape(25.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TextColor,
            unfocusedBorderColor = Color.LightGray
                                                 )
                     )
}