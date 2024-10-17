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

// ViewModel para manejar el Queue
class QueueViewModel(private val valuesList: List<Int>) : ViewModel() {
    private val _queue = mutableStateListOf<Int>()
    val queue: List<Int> get() = _queue

    private var currentIndex = 0  // Para iterar por los elementos de valuesList
    var peekedIndex by mutableStateOf<Int?>(null)
        private set

    // Función para agregar el siguiente elemento a la cola
    fun enqueueNextElement() {
        if (currentIndex < valuesList.size) {
            _queue.add(valuesList[currentIndex])
            currentIndex++
            clearPeek()
        }
    }

    // Función para eliminar el primer elemento de la cola
    fun dequeue() {
        if (_queue.isNotEmpty()) {
            _queue.removeAt(0)  // Elimina el primer elemento de la cola (FIFO)
            clearPeek()
        }
    }

    // Peek muestra el primer elemento de la cola sin eliminarlo
    fun peek() {
        if (_queue.isNotEmpty()) {
            peekedIndex = 0  // El primer elemento de la cola es el que se peekeó
        }
    }

    // Limpiar el valor de peek
    fun clearPeek() {
        peekedIndex = null
    }
}

@Composable
fun QueuesVisualizer(viewModel: QueueViewModel) {

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
                viewModel.enqueueNextElement()
            }) {
                Text("Enqueue")
            }
            Button(onClick = {
                viewModel.dequeue()
            }) {
                Text("Dequeue")
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

        // Mostrar los elementos de la cola
        viewModel.queue.forEachIndexed { index, item ->
            val backgroundColor = if (index == viewModel.peekedIndex) Color.Yellow else Color.LightGray  // Cambiar el color si es el elemento peekeado
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
