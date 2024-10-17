package com.example.datastructurevisualizerapp.screens

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.res.colorResource
import com.example.datastructurevisualizerapp.R
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun userProfileScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    // Inicializar FirebaseAuth
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    // Variables para mostrar los datos del usuario
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") } // No se puede obtener la contraseña directamente desde Firebase por motivos de seguridad

    // Cargar los datos del usuario si está autenticado
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            user.displayName?.let { userName = it }
            userEmail = user.email ?: "Email no disponible"
        }
    }

    val ovalColor = colorResource(id = R.color.mainColor)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil del Usuario",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.mainColor),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Box(
            modifier = Modifier.size(140.dp).padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(100.dp)) {
                drawOval(color = ovalColor, size = this.size)
            }
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = colorResource(R.color.white),
                modifier = Modifier.size(110.dp)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto con el nombre del usuario
        OutlinedTextField(
            value = userName,
            onValueChange = { /* No se puede editar */ },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto con el email del usuario
        OutlinedTextField(
            value = userEmail,
            onValueChange = { /* No se puede editar */ },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.size(32.dp))

        // Botón para cerrar sesión
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()  // Cerrar sesión en Firebase
                onLogout()  // Cambiar isLoggedIn a false
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }  // Limpiar la pila de navegación
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}

