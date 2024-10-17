package com.example.datastructurevisualizerapp.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

// ViewModel para manejar el Queue
class QueueViewModel(private val valuesList: List<Double>) : ViewModel() {
    private val _queue = mutableStateListOf<Double>()
    val queue: List<Double> get() = _queue

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
            .padding(12.dp)
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

        // Dibuja la cola usando Canvas
        QueueCanvas(viewModel.queue, viewModel.peekedIndex)
    }
}

@Composable
fun QueueCanvas(queue: List<Double>, peekedIndex: Int?) {
    // Obtenemos el ancho del dispositivo
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp * configuration.densityDpi / 160  // Convertimos dp a px

    if (queue.isNotEmpty()) {
        // Definimos el tamaño del Canvas, ajustando para que haya varias filas
        val nodeSpacing = 150f  // Espaciado horizontal entre los nodos
        val rowHeight = 150f    // Altura de cada fila
        val canvasWidth = screenWidthPx.toFloat()-225  // Usamos el ancho completo del dispositivo

        // Calculamos el número de filas basado en el tamaño de la cola
        val numRows = (queue.size * nodeSpacing / canvasWidth).toInt() + 1
        val canvasHeight = numRows * rowHeight + 100f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(canvasHeight.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var xPosition = 100f  // Posición inicial en el eje X
                var yPosition = 100f  // Posición inicial en el eje Y

                queue.forEachIndexed { index, value ->
                    // Cambiar el color del elemento si está peekeado
                    var color = if (index == peekedIndex) Color.Yellow else Color.Green

                    // Dibuja el nodo como un círculo
                    drawCircle(
                        color = color,
                        radius = 40f,
                        center = Offset(xPosition, yPosition),
                        style = Stroke(width = 4f)
                    )

                    // Dibuja el valor del nodo dentro del círculo
                    drawContext.canvas.nativeCanvas.drawText(
                        value.toString(),
                        xPosition - 20f,
                        yPosition + 15f,
                        android.graphics.Paint().apply {
                            textSize = 40f
                            color = Color.Black
                        }
                    )

                    // Dibuja la línea con flecha que conecta al siguiente nodo
                    if (index < queue.size - 1) {
                        val nextXPosition = if (xPosition + nodeSpacing > canvasWidth) 100f else xPosition + nodeSpacing
                        val nextYPosition = if (xPosition + nodeSpacing > canvasWidth) yPosition + rowHeight else yPosition

                        drawArrow(
                            start = Offset(xPosition + 40f, yPosition),
                            end = Offset(nextXPosition - 40f, nextYPosition)
                        )

                        // Actualizamos las posiciones para el próximo nodo
                        xPosition = nextXPosition
                        yPosition = nextYPosition
                    }
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

// Función para dibujar una línea con flecha entre dos puntos
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawArrow(start: Offset, end: Offset) {
    // Dibuja la línea
    drawLine(
        color = Color.Black,
        start = start,
        end = end,
        strokeWidth = 4f
    )

    // Calculamos el ángulo de la flecha
    val arrowAngle = atan2(end.y - start.y, end.x - start.x)
    val arrowLength = 20f

    // Puntos para las líneas de la flecha
    val arrowPoint1 = Offset(
        end.x - arrowLength * cos(arrowAngle - Math.PI.toFloat() / 6f),
        end.y - arrowLength * sin(arrowAngle - Math.PI.toFloat() / 6f)
    )
    val arrowPoint2 = Offset(
        end.x - arrowLength * cos(arrowAngle + Math.PI.toFloat() / 6f),
        end.y - arrowLength * sin(arrowAngle + Math.PI.toFloat() / 6f)
    )

    // Dibuja la flecha (dos líneas convergentes)
    val arrowPath = Path().apply {
        moveTo(end.x, end.y)
        lineTo(arrowPoint1.x, arrowPoint1.y)
        moveTo(end.x, end.y)
        lineTo(arrowPoint2.x, arrowPoint2.y)
    }
    drawPath(path = arrowPath, color = Color.Black, style = Stroke(width = 4f))
}
