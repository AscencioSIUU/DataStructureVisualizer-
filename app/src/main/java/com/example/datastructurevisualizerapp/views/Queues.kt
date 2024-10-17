package com.example.datastructurevisualizerapp.views

import android.graphics.Color.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
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

        LazyRow {
            item {
                // Dibuja la cola usando Canvas
                QueueCanvas(viewModel.queue, viewModel.peekedIndex)
            }
        }
    }
}

@Composable
fun QueueCanvas(queue: List<Int>, peekedIndex: Int?) {
    if (queue.isNotEmpty()) {
        // Calcula el ancho del canvas basado en la cantidad de elementos en la cola
        val canvasWidth = queue.size * 150f + 100f

        Box(
            modifier = Modifier
                .width(canvasWidth.dp)
                .height(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Dibuja los elementos de la cola como círculos conectados por líneas
                val nodeSpacing = 150f  // Espaciado horizontal entre los nodos
                var xPosition = 100f  // Posición inicial en el eje X

                queue.forEachIndexed { index, value ->
                    // Cambiar el color del elemento si está peekeado
                    var color = if (index == peekedIndex) Color.Yellow else Color.Green

                    // Dibuja el nodo como un círculo
                    drawCircle(
                        color = color,
                        radius = 40f,
                        center = Offset(xPosition, size.height / 2),
                        style = Stroke(width = 4f)
                    )

                    // Dibuja el valor del nodo dentro del círculo
                    drawContext.canvas.nativeCanvas.drawText(
                        value.toString(),
                        xPosition - 20f,
                        size.height / 2 + 15f,
                        android.graphics.Paint().apply {
                            textSize = 40f
                            color = Color.Black
                        }
                    )

                    // Dibuja la línea que conecta al siguiente nodo
                    if (index < queue.size - 1) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(xPosition + 40f, size.height / 2),
                            end = Offset(xPosition + nodeSpacing - 40f, size.height / 2),
                            strokeWidth = 4f
                        )
                    }

                    // Incrementa la posición X para el siguiente nodo
                    xPosition += nodeSpacing
                }
            }
        }
    } else {
        // Mensaje para cuando la cola esté vacía
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("La cola está vacía. Agrega un valor.")
        }
    }
}
