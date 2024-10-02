package com.example.datastructurevisualizerapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

@Composable
fun StackVisualizer(viewModel: StackViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mostrar los elementos del stack
        viewModel.stack.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(50.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botones para las operaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.push("Elemento") }) {
                Text("Push")
            }
            Button(onClick = { viewModel.pop() }) {
                Text("Pop")
            }
            Button(onClick = { viewModel.peek() }) {
                Text("Peek")
            }
        }
    }
}


// ViewModel para manejar el Stack
class StackViewModel : ViewModel() {
    private val _stack = mutableStateListOf<String>()
    val stack: List<String> get() = _stack

    fun push(item: String) {
        _stack.add(item)
    }

    fun pop() {
        if (_stack.isNotEmpty()) {
            _stack.removeAt(_stack.size - 1)
        }
    }

    fun peek() {
        if (_stack.isNotEmpty()) {
            // Realiza alguna acción de visualización
        }
    }
}
