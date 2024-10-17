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
    user: String,
    email: String,
    password: String,
    navController: NavController,
    onLogout: () -> Unit  // Agregar función para manejar el cierre de sesión
){
    var userName by remember { mutableStateOf(user) }
    var userEmail by remember { mutableStateOf(email) }
    var userPassword by remember { mutableStateOf(password) }

    val ovalColor = colorResource(id = R.color.mainColor)

    val isFormValid = userName.isNotBlank() && userEmail.isNotBlank() && userPassword.isNotBlank()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.user_profile),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.mainColor),
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Box(
            modifier = Modifier
                .size(140.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(100.dp)){
                drawOval(
                    color = ovalColor,
                    size = this.size
                )
            }
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = colorResource(R.color.white),
                modifier = Modifier.size(110.dp),
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = { /* No se puede editar */ },
            label = { Text("Nombre") },
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = userEmail,
            onValueChange = { /* No se puede editar */ },
            label = { Text("Email") },
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = userPassword,
            onValueChange = { /* No se puede editar */ },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.size(32.dp))
        FilledTonalButton(
            onClick = { /*TODO: Guardar cambios si es necesario */ },
            enabled = isFormValid,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (isFormValid) MaterialTheme.colorScheme.secondary else Color.Gray,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.size(32.dp))

        // Botón de cerrar sesión
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()  // Cerrar sesión en Firebase
                onLogout()  // Cambiar isLoggedIn a false
                navController.navigate("login") {  // Navegar a la pantalla de login
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
