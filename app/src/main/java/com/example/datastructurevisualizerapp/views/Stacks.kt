package com.example.datastructurevisualizerapp.views

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel

@Composable
fun StackVisualizer(viewModel: StackViewModel) {

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) } // Para el índice del elemento seleccionado en el Dropdown
    var peekedIndex by remember { mutableStateOf<Int?>(null) } // Controla el índice del elemento peekeado en la UI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mostrar los elementos del stack
        viewModel.stack.forEachIndexed { index, item ->
            val backgroundColor = if (index == peekedIndex) Color.Yellow else Color.LightGray  // Cambiar el color si es el elemento peekeado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(50.dp)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown para seleccionar el elemento que se quiere "peekear"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Text(
                    text = if (selectedIndex == -1) "Selecciona un elemento para hacaer peek" else viewModel.stack.getOrNull(selectedIndex) ?: "Selecciona un elemento para hacaer peek",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .padding(16.dp)
                        .background(Color.LightGray)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.stack.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para las operaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.pushNextElement() }) {
                Text("Push")
            }
            Button(onClick = {
                viewModel.pop()
                // Si el elemento que fue peekeado o seleccionado fue removido, limpiar selección
                if (selectedIndex >= viewModel.stack.size) {
                    selectedIndex = -1 // Reinicia la selección
                    peekedIndex = null // Limpia el elemento peekeado
                }
            }) {
                Text("Pop")
            }
            Button(onClick = {
                if (selectedIndex != -1) {
                    peekedIndex = selectedIndex // Actualiza el estado del elemento peekeado
                    viewModel.peekAtIndex(selectedIndex) // Actualiza también el ViewModel si es necesario
                }
            }) {
                Text("Peek")
            }
            Button(onClick = {
                peekedIndex = null // Limpia el elemento peekeado en la UI
                viewModel.clearPeek() // Limpia también en el ViewModel
            }) {
                Text("Limpiar Peek")
            }
        }
    }
}

// ViewModel para manejar el Stack
class StackViewModel : ViewModel() {
    private val _stack = mutableStateListOf<String>()
    val stack: List<String> get() = _stack

    // Lista quemada de datos
    private val predefinedElements = listOf("Elemento 1", "Elemento 2", "Elemento 3", "Elemento 4", "Elemento 5")
    private var currentIndex = 0  // Para ir iterando por los elementos

    // Variable para guardar el índice del elemento peekeado
    var peekedIndex: Int? = null
        private set

    // Función para pushear el siguiente elemento de la lista
    fun pushNextElement() {
        if (currentIndex < predefinedElements.size) {
            _stack.add(predefinedElements[currentIndex])
            currentIndex++
        }
    }

    fun pop() {
        if (_stack.isNotEmpty()) {
            _stack.removeAt(_stack.size - 1)
            if (currentIndex > 0) {
                currentIndex--  // Retrocede el índice cuando haces pop
            }
        }
    }

    // Peeking en un índice específico seleccionado por el usuario
    fun peekAtIndex(index: Int) {
        if (index in _stack.indices) {
            peekedIndex = index
        }
    }

    fun clearPeek() {
        peekedIndex = null // Para desactivar el peek y volver al color normal
    }
}