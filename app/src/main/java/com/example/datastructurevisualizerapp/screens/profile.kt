package com.example.datastructurevisualizerapp.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

@Composable
fun userProfileScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val databaseRef = FirebaseDatabase.getInstance().getReference("users/${currentUser?.uid}")

    var userName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var userEmail by remember { mutableStateOf(currentUser?.email ?: "") }
    var userPassword by remember { mutableStateOf("") }
    var userPhoto by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    // Configuración de la cámara
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        userPhoto = bitmap
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Cargar la foto desde Firebase Realtime Database
    LaunchedEffect(currentUser) {
        currentUser?.let {
            databaseRef.child("profileImage").get().addOnSuccessListener { snapshot ->
                val imageBase64 = snapshot.value as? String
                if (!imageBase64.isNullOrEmpty()) {
                    val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
                    userPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                },
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
                    tint = colorResource(R.color.mainColor),
                    modifier = Modifier.size(110.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Nueva Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = {
                // Actualizar el nombre en Firebase Authentication
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()

                currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Nombre actualizado con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show()
                    }
                }

                // Actualizar el email en Firebase Authentication
                currentUser?.updateEmail(userEmail)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Email actualizado con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al actualizar el email", Toast.LENGTH_SHORT).show()
                    }
                }

                // Actualizar la contraseña si no está vacía
                if (userPassword.isNotEmpty()) {
                    currentUser?.updatePassword(userPassword)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // Guardar la foto en Firebase Realtime Database
                val imageBase64 = userPhoto?.let { bitmap ->
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val byteArray = byteArrayOutputStream.toByteArray()
                    Base64.encodeToString(byteArray, Base64.DEFAULT)
                }

                // Guardar los datos del perfil en Realtime Database
                val userProfile = mapOf(
                    "name" to userName,
                    "email" to userEmail,
                    "profileImage" to imageBase64
                )

                databaseRef.setValue(userProfile).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Perfil actualizado en la base de datos", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
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

        Button(
            onClick = {
                firebaseAuth.signOut()
                onLogout()
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
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
