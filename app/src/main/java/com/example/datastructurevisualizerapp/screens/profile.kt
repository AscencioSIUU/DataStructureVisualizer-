package com.example.datastructurevisualizerapp.screens

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun userProfileScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    // Inicializar FirebaseAuth
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    // Variables para mostrar y editar los datos del usuario
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") } // Usado para cambiar la contraseña

    //Variables para la camara
    var userPhoto by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        userPhoto = bitmap // Asigna el Bitmap de la foto tomada
    }

    // Obtener el contexto actual para mostrar mensajes de Toast
    val context = LocalContext.current

    // Launcher para solicitar permisos de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }



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
            modifier = Modifier
                .size(140.dp)
                .padding(8.dp)
                .clickable {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }, // Solicita el permiso de cámara al hacer clic
                contentAlignment = Alignment.Center
        ) {
            if (userPhoto != null) {
                Image(
                    bitmap = userPhoto!!.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(110.dp)
                )
            } else {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = ovalColor,
                    modifier = Modifier.size(110.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto con el nombre del usuario editable
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto con el email del usuario editable
        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto para cambiar la contraseña
        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Nueva Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.size(32.dp))

        // Botón para actualizar el perfil (nombre, correo y contraseña)
        Button(
            onClick = {
                // Actualizar nombre
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()

                currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Nombre actualizado con éxito")
                    } else {
                        println("Error al actualizar el nombre: ${task.exception?.message}")
                    }
                }

                // Actualizar email
                currentUser?.updateEmail(userEmail)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Email actualizado con éxito")
                    } else {
                        println("Error al actualizar el email: ${task.exception?.message}")
                    }
                }

                // Actualizar contraseña si no está vacío
                if (userPassword.isNotEmpty()) {
                    currentUser?.updatePassword(userPassword)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Contraseña actualizada con éxito")
                        } else {
                            println("Error al actualizar la contraseña: ${task.exception?.message}")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            )
        ) {
            Text("Actualizar Perfil")
        }

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
