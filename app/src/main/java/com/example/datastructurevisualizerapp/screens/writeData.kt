package com.example.datastructurevisualizerapp.screens

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
fun WriteData(navController: NavController){
    Column (
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
        //Ingreso de datos
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Ingrese sus datos") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Titulo 2
        Text(
            text = "O suba su archivo .CSV:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFF1A2A3A)
        )
        Button(
            onClick = { /* Implementar logica */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Text("Seleccione un archivo", color = Color(0xFF1A2A3A))
        }
    }
}

