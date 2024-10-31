package com.example.datastructurevisualizerapp.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.datastructurevisualizerapp.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WriteData(navController: NavController, onCsvSelectClick: () -> Unit) {
    // Estado para almacenar el valor ingresado en el campo de texto
    var textInput by remember { mutableStateOf("") }
    var numberList by remember { mutableStateOf<List<Int>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Ingrese los datos a mostrar:",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF1A2A3A)
        )

        // Subtitulo
        Text(
            text = "Sus datos deben estar separados por comas. Ej: 1,2,3,4",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.Center,
            color = Color(0xFF1A2A3A)
        )

        // Ingreso de datos manuales
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Ingrese sus datos") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para procesar los datos ingresados manualmente
        Button(
            onClick = {
                // Procesar el texto ingresado y convertirlo a una lista de números
                numberList = textInput.split(",").mapNotNull { it.trim().toIntOrNull() }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Guardar datos", color = Color(0xFF1A2A3A))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar la lista de números guardados manualmente
        if (numberList.isNotEmpty()) {
            Text(
                text = "Datos guardados: ${numberList.joinToString(", ")}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1A2A3A)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Titulo para la opción de subir archivo
        Text(
            text = "O suba su archivo .CSV:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF1A2A3A)
        )

        // Botón para seleccionar un archivo CSV
        Button(
            onClick = {
                onCsvSelectClick() // Llamada a la función para abrir el selector de archivos
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Seleccione un archivo", color = Color(0xFF1A2A3A))
        }
    }
}


