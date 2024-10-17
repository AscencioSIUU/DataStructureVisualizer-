package com.example.datastructurevisualizerapp.screens

import LoginScreenViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.navOptions

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
    onLogin: (String, String, String) -> Unit
) {    // Variables para almacenar el texto de entrada
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Iniciar Sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF1A2A3A)
        )
/*
        // Campo de texto para el nombre
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
*/
        // Campo de texto para el email
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Botones


        Button(
            onClick = {
                viewModel.signInWithEmailAndPassword(
                    email.value.text,
                    password.value.text,
                    {
                        onLogin(name.value.text, email.value.text, password.value.text)
                        navController.navigate("home")
                    },
                    { exception ->
                        // Manejar errores si el inicio de sesión falla
                        println("Error: ${exception.message}")
                    }
                )
            },
            enabled =  email.value.text.isNotEmpty() && password.value.text.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 30.dp)
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 16.sp,
                color = Color.White // Texto blanco
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto de enlace para ir a registro
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? ")
            Text(
                text = "Regístrate",
                color = Color(0xFF0F9D58),
                modifier = Modifier.clickable {
                    navController.navigate("createAccount")
                }
            )
        }

    }
}
