package com.example.datastructurevisualizerapp.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.MainActivity
import com.example.datastructurevisualizerapp.viewmodels.DbViewModel

@Composable
fun WriteData(
    navController: NavController,
    dbViewModel: DbViewModel,
    onCsvSelectClick: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    // Observa los datos ingresados manualmente y del CSV por separado
    val manualData by dbViewModel.datosManuales.collectAsState()
    val csvData by dbViewModel.datosCsv.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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

        // Subtítulo
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
                // Procesar el texto ingresado y actualizar la lista en el ViewModel
                val numbers = textInput.split(",").mapNotNull { it.trim().toIntOrNull() }
                dbViewModel.updateManualData(numbers)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Guardar datos", color = Color(0xFF1A2A3A))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar solo la lista de números guardados manualmente
        if (manualData.isNotEmpty()) {
            Text(
                text = "Datos guardados manualmente: ${manualData.joinToString(", ")}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1A2A3A)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Título para la opción de subir archivo
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
                onCsvSelectClick() // Llamada al lambda para abrir el selector de archivos
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Seleccione un archivo", color = Color(0xFF1A2A3A))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar los primeros 3 valores del archivo CSV si existen
        if (csvData.isNotEmpty()) {
            Text(
                text = "Primeros 3 valores del archivo: ${csvData.take(3).joinToString(", ")}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1A2A3A)
            )
        }
    }
}


