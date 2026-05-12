package com.example.nutriai

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    val totalPages = 8
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    } else {
                        onNavigateBack() // Vuelve al registro
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
                            onFinishOnboarding() // Termina y va al Home
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TextColor) // Botón negro como en tu diseño
                      ) {
                    Text("Continuar", fontSize = 16.sp, color = Color.White)
                }
            }
        }
            ) { paddingValues ->
        // El Carrusel de preguntas
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            userScrollEnabled = false // Evita que pasen de página deslizando el dedo, obliga a usar el botón
                       ) { page ->
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {

                // --- TÍTULOS Y SUBTÍTULOS DINÁMICOS ---
                val title = when(page) {
                    0 -> "Elige tu género"
                    1 -> "¿Para cuántos cocinas?"
                    2 -> "¿Cuál es tu nivel en la cocina?"
                    // ... añade los demás
                    else -> "Pregunta ${page + 1}"
                }

                val subtitle = when(page) {
                    0 -> "Esto se usará para calibrar tu plan personalizado."
                    1 -> "Ajustaremos las cantidades de los ingredientes a tus necesidades."
                    // ... añade los demás
                    else -> "Subtítulo descriptivo."
                }

                Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(32.dp))

                // --- CONTENIDO CENTRAL DINÁMICO ---
                when (page) {
                    0 -> // PÁGINA 1: Género
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OnboardingOptionButton("Hombre", isSelected = true)
                            OnboardingOptionButton("Mujer", isSelected = false)
                            OnboardingOptionButton("Otro", isSelected = false)
                        }
                    1 -> // PÁGINA 2: Para cuántos
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OnboardingOptionButton("Solo para mi", isSelected = true)
                            OnboardingOptionButton("Para 2 personas", isSelected = false)
                            OnboardingOptionButton("3 personas", isSelected = false)
                        }
                    // AQUÍ TENDRÁS QUE AÑADIR LAS DEMÁS PÁGINAS (2, 3, 4, 5, 6, 7)
                    else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Contenido de la página ${page + 1} en construcción")
                    }
                }
            }
        }
    }
}

// Composable auxiliar para dibujar los botones blancos/negros de las opciones
@Composable
fun OnboardingOptionButton(text: String, isSelected: Boolean) {
    Button(
        onClick = { /* Lógica para guardar la selección */ },
        modifier = Modifier.fillMaxWidth().height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) TextColor else BackgroundColor,
            contentColor = if (isSelected) Color.White else TextColor
                                            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if(isSelected) 4.dp else 0.dp)
          ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}