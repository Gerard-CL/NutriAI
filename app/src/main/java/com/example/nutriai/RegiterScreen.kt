package com.example.nutriai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("Karim Santel") }
    var email by remember { mutableStateOf("karim@gmail.com") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.Start
          ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text("Crear Cuenta", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Verification successful. Create your log in password", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(40.dp))

        // Campo Nombre
        Text("Name", color = NutriGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = name, onValueChange = { name = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray, unfocusedIndicatorColor = Color.LightGray
                                             ),
            modifier = Modifier.fillMaxWidth()
                 )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Email
        Text("Email", color = NutriGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = email, onValueChange = { email = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray, unfocusedIndicatorColor = Color.LightGray
                                             ),
            modifier = Modifier.fillMaxWidth()
                 )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Contraseña
        Text("Create Password", color = NutriGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = password, onValueChange = { password = it },
            placeholder = { Text("Password", color = Color.LightGray) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray, unfocusedIndicatorColor = Color.LightGray
                                             ),
            modifier = Modifier.fillMaxWidth()
                 )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Confirmar Contraseña
        TextField(
            value = confirmPassword, onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm Password", color = Color.LightGray) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray, unfocusedIndicatorColor = Color.LightGray
                                             ),
            modifier = Modifier.fillMaxWidth()
                 )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

        // Botón Done
        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NutriGreen)
              ) {
            Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}