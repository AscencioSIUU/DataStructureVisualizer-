package com.example.datastructurevisualizerapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

// ViewModel para manejar el Stack
class StackViewModel(private val valuesList: List<Double>) : ViewModel() {
    private val _stack = mutableStateListOf<Double>()
    val stack: List<Double> get() = _stack

    private var currentIndex = 0  // Para iterar por los elementos de valuesList

    // Variable para guardar el índice del elemento peekeado
    var peekedIndex by mutableStateOf<Int?>(null)
        private set

    // Función para pushear el siguiente elemento de valuesList
    fun pushNextElement() {
        if (currentIndex < valuesList.size) {
            _stack.add(valuesList[currentIndex])
            currentIndex++
            // Limpiar el peekedIndex al agregar un nuevo elemento
            clearPeek()
        }
    }

    fun pop() {
        if (_stack.isNotEmpty()) {
            _stack.removeAt(_stack.size - 1)
            if (currentIndex > 0) {
                currentIndex--  // Retrocede el índice cuando haces pop
            }
            // Limpiar el peekedIndex al hacer pop
            clearPeek()
        }
    }

    // Peeking al último elemento agregado cuando se presiona el botón Peek
    fun peek() {
        if (_stack.isNotEmpty()) {
            peekedIndex = _stack.size - 1
        }
    }

    fun clearPeek() {
        peekedIndex = null // Para desactivar el peek y volver al color normal
    }
}

@Composable
fun StackVisualizer(viewModel: StackViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        // Botones para las operaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                viewModel.pushNextElement()
            }) {
                Text("Push")
            }
            Button(onClick = {
                viewModel.pop()
            }) {
                Text("Pop")
            }
            Button(onClick = {
                viewModel.peek()
            }) {
                Text("Peek")
            }
            Button(onClick = {
                viewModel.clearPeek()
            }) {
                Text("Limpiar Peek")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los elementos del stack
        viewModel.stack.reversed().forEachIndexed { index, item ->
            val actualIndex = viewModel.stack.size - 1 - index  // Ajustar el índice para que coincida con peekedIndex
            val backgroundColor = if (actualIndex == viewModel.peekedIndex) Color.Yellow else Color.LightGray  // Cambiar el color si es el elemento peekeado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(50.dp)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.toString())
            }
        }

    }
}
