package com.example.nutriai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data class actualizada con la ruta de navegación
data class SettingsOption(val title: String, val route: String)

@Composable
fun SettingsScreen(
    onNavigateToInicio: () -> Unit,
    onNavigateToBasicos: () -> Unit,
    onNavigateBack: () -> Unit,
    // 2. Nueva función para navegar al detalle de cada ajuste
    onNavigateToSettingDetail: (String) -> Unit
                  ) {
    // 3. Lista de opciones vinculadas a sus rutas exactas
    val options = listOf(
        SettingsOption("Elige tu género", "settings_gender"),
        SettingsOption("Para cuántos cocinas", "settings_portions"),
        SettingsOption("Tu nivel en la cocina", "settings_level"),
        SettingsOption("Tiempo para cocinar", "settings_time"),
        SettingsOption("Alergías", "settings_allergies"),
        SettingsOption("Alimentos no te gustan", "settings_disliked")
                        )

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            SettingsHeader(onBackClick = onNavigateBack)
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "config",
                onInicioClick = onNavigateToInicio,
                onBasicosClick = onNavigateToBasicos,
                onConfigClick = { /* Ya estamos en configuración */ }
                               )
        }
            ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
            items(options) { option ->
                SettingsRow(
                    option = option,
                    // 4. Mandamos la ruta cuando el usuario toca la fila
                    onClick = { onNavigateToSettingDetail(option.route) }
                           )
            }
        }
    }
}

@Composable
fun SettingsHeader(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 16.dp)
          ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                color = TextColor
                )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
           ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
                      ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextColor
                    )
            }
            Text(
                text = "Configuración",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextColor
                )
        }
    }
}

@Composable
fun SettingsRow(option: SettingsOption, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // 5. Hace la fila clicable
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
       ) {
        Text(
            text = option.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextColor
            )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Flecha",
            tint = TextColor,
            modifier = Modifier.size(24.dp)
            )
    }
    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
}