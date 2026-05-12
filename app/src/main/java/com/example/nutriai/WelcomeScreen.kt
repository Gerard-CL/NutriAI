package com.example.nutriai // Cambia esto por tu paquete real

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource // <--- Asegúrate de tener tu logo aquí
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- DEFINICIÓN DE COLORES (Si no los tienes) ---
// El verde específico de tu logo


@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
                 ) {
    Scaffold(
        containerColor = BackgroundColor // Match image background
            ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp), // Overall padding
            horizontalAlignment = Alignment.CenterHorizontally
              ) {
            // 1. Top Bar: Logo + NutriAI
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // Left aligned like image
               ) {
                // Custom Logo (User needs to provide R.drawable.logo_nutriai)
                Image(
                    painter = painterResource(id = R.drawable.logo_nutriai), // <--- IMPORTANTE: Añade tu logo aquí
                    contentDescription = "Logo NutriAI",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape) // Rounded background as in image
                        .background(NutriGreen.copy(alpha = 0.1f)) // Faint green bg
                        .padding(8.dp) // Space inside background
                     )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "NutriAI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextColor
                    )
            }

            // 2. Main Center Content (Main Texts)
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(), // Middle section pushes bottom section down
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Vertically centered main texts
                  ) {
                Text(
                    text = "Bienvenido a",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextColor,
                    textAlign = TextAlign.Center
                    )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NUTRI.AI",
                    fontSize = 50.sp, // Large and Bold
                    fontWeight = FontWeight.ExtraBold,
                    color = NutriGreen, // The green color
                    textAlign = TextAlign.Center
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Inicia sesión para disfrutar de nuestros servicios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray, // Smaller and Gray
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp) // Limit width slightly
                    )
            }

            // 3. Bottom Section: Login/Register Buttons
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), // Bottom padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Space between buttons
                  ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    textAlign = TextAlign.Center
                    )

                // "Iniciar Sesión" Button (Outlined/Border style)
                OutlinedButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(25.dp), // Rounded borders
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White, // White background
                        contentColor = TextColor // Black text
                                                                ),
                    border = BorderStroke(1.dp, TextColor)
                              ) {
                    Text(text = "Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                // "Registrarse" Button (Filled green style with shadow)
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(25.dp), // Rounded borders
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriGreen, // Green background
                        contentColor = Color.White // White text
                                                        ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp) // Subtle shadow like in image
                      ) {
                    Text(text = "Registrarse", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}