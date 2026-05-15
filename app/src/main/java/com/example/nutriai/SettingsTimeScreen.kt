package com.example.nutriai

import androidx.compose.foundation.layout.*
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
fun SettingsTimeScreen(viewModel: RecipeViewModel, onNavigateBack: () -> Unit) {
    val savedTime by viewModel.userTime.collectAsState()
    var tempSelection by remember(savedTime) { mutableStateOf(savedTime) }

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
                        viewModel.setTime(tempSelection)
                        onNavigateBack()
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
            Text("¿Cuánto tiempo sueles tener?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextColor)
            Spacer(modifier = Modifier.height(32.dp))

            val options = listOf("Menos de 15 minutos", "De 15 a 30 minutos", "De 30 a 60 minutos", "Más de una hora")
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                options.forEach { option ->
                    OnboardingOptionButton(
                        text = option,
                        isSelected = tempSelection == option,
                        selectedColor = NutriGreen,
                        onClick = { tempSelection = option }
                                          )
                }
            }
        }
    }
}