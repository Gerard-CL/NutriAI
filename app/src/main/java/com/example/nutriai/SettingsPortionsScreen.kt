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
fun SettingsPortionsScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit
                          ) {
    // 1. Leemos lo que el usuario tenía guardado en el Cerebro global
    val savedPortions by viewModel.userPortions.collectAsState()

    // 2. Creamos un "borrador" local.
    // Si cambia de opinión pero le da a la flecha de atrás sin guardar, no se rompe nada.
    var tempSelection by remember(savedPortions) { mutableStateOf(savedPortions) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(8.dp)) {
                // Flecha verde como en tu diseño
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = NutriGreen)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Button(
                    onClick = {
                        // 3. ¡Lo guardamos en el Cerebro!
                        viewModel.setPortions(tempSelection)
                        // 4. Volvemos atrás
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
            ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
              ) {
            Text("¿Para cuántos cocinas?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ajustaremos las cantidades de los ingredientes a tus necesidades.", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            val options = listOf("Solo para mi", "Para 2 personas", "3 personas", "4 personas", "Familias grandes (5+)")

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                options.forEach { option ->
                    // 5. Llamamos a tu pieza de Lego, pero forzamos que sea Verde
                    OnboardingOptionButton(
                        text = option,
                        isSelected = tempSelection == option,
                        selectedColor = NutriGreen, // <--- MAGIA: Aquí lo hacemos verde
                        onClick = { tempSelection = option }
                                          )
                }
            }
        }
    }
}