package com.example.nutriai

import android.os.Bundle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Colores personalizados
val NutriGreen = Color(0xFF10B981)
val BackgroundColor = Color(0xFFF8F9FA)
val TextColor = Color(0xFF1A1A1A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        // Pantalla 1: Inicio
        composable("home") {
            NutriAppScreen(
                onNavigateToCreate = { navController.navigate("create_recipe") },
                onNavigateToBasicos = {
                    navController.navigate("mis_basicos") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToConfig = {
                    navController.navigate("config") {
                        popUpTo("home") { inclusive = false }
                    }
                }
                          )
        }

        // Pantalla 2: Crear Receta (Elegir ingredientes)
        // Pantalla 2: Crear Receta
        composable("create_recipe") {
            CreateRecipeScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipes = { selectedIngredients ->
                    // 1. Le damos los ingredientes al ViewModel
                    recipeViewModel.setIngredientsAndSearch(selectedIngredients)
                    // 2. Navegamos "limpios", sin variables en la ruta
                    navController.navigate("generated_recipes")
                }
                              )
        }

        // Pantalla 2.5: Lista de Recetas Generadas (Ruta limpia)
        composable("generated_recipes") {
            GeneratedRecipesScreen(
                viewModel = recipeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { recipeTitle ->
                    // 1. Le decimos al ViewModel qué receta se ha tocado
                    recipeViewModel.selectRecipe(recipeTitle)
                    // 2. Navegamos al detalle "limpios"
                    navController.navigate("recipe_detail")
                }
                                  )
        }

        // Ruta del detalle (Ruta limpia)
        composable("recipe_detail") {
            RecipeDetailScreen(
                viewModel = recipeViewModel,
                onNavigateBack = { navController.popBackStack() }
                              )
        }

        // Pantalla 3: Mis Básicos
        composable("mis_basicos") {
            MisBasicosScreen(
                viewModel = recipeViewModel,
                onNavigateToInicio = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToConfig = {
                    navController.navigate("config") {
                        popUpTo("home") { inclusive = false }
                    }
                }
                            )
        }

        // Pantalla 4: Configuración
        composable("config") {
            SettingsScreen(
                onNavigateToInicio = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToBasicos = {
                    navController.navigate("mis_basicos") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
                          )
        }
    }
}

@Composable
fun NutriAppScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToBasicos: () -> Unit,
    onNavigateToConfig: () -> Unit
                  ) {
    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onInicioClick = { /* Ya estamos en Inicio */ },
                onBasicosClick = onNavigateToBasicos,
                onConfigClick = onNavigateToConfig
                               )
        },
        floatingActionButton = { FloatingCenterButton(onClick = {/* Nada por ahora */}) },
        floatingActionButtonPosition = FabPosition.Center
            ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
                  ) {
            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { TopBar() }
            item { GreetingSection() }
            item { CreateRecipeButton(onClick = onNavigateToCreate) }
            item { SectionTitle("Recetas Favoritas") }

            items(4) { index ->
                RecipeCard(
                    title = if (index < 2) "Crostas de Pollo" else "Tortilla de Patata",
                    calories = "200 Calorias",
                    time = if (index < 2) "20 min" else "25 min"
                          )
            }
            item { Spacer(modifier = Modifier.height(60.dp)) }
        }
    }
}

// ... (TopBar, GreetingSection, CreateRecipeButton, SectionTitle, RecipeCard, FloatingCenterButton se quedan igual que los tenías) ...
@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
       ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.logo_nutriai),
                contentDescription = "Logo",
                tint = NutriGreen,
                modifier = Modifier.size(32.dp)
                )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "NutriAI", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextColor)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            BadgedBox(badge = { Badge(containerColor = NutriGreen) }) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Perfil", modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun GreetingSection() {
    Text(text = "Bienvenido, JUAN", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = TextColor, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
}

@Composable
fun CreateRecipeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = NutriGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().height(80.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
          ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.gorro_chef), contentDescription = "Gorro de Chef", tint = Color.White, modifier = Modifier.size(45.dp)) // Ajusta el icono si tienes el painter
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "CREAR RECETAS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextColor)
}

@Composable
fun RecipeCard(title: String, calories: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(Color.LightGray))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = calories, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = time, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(text = "🥩 20 g", fontSize = 10.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "🍞 80 g", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun FloatingCenterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = NutriGreen,
        contentColor = Color.White,
        shape = CircleShape,
        modifier = Modifier.offset(y = 20.dp)
                        ) {
        Icon(Icons.Default.List, contentDescription = "Básicos", modifier = Modifier.size(28.dp))
    }
}

// ------------------------------------------------------------------
// BARRA DE NAVEGACIÓN COMPARTIDA (LA USAMOS EN TODAS LAS PANTALLAS)
// ------------------------------------------------------------------
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onInicioClick: () -> Unit = {},
    onBasicosClick: () -> Unit = {},
    onConfigClick: () -> Unit = {}
                       ) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
                 ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = onInicioClick
                         )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Básicos") },
            label = { Text("Básicos") },
            selected = currentRoute == "mis_basicos",
            onClick = onBasicosClick
                         )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            label = { Text("Configuración") },
            selected = currentRoute == "config",
            onClick = onConfigClick
                         )
    }
}