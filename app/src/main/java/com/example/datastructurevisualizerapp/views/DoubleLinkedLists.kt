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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Path


@Composable
fun DoubleLinkedListsVisualizer(initialList: List<Double>) {
    val nodeList = remember { mutableStateListOf<Double>() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Botones para agregar y eliminar
        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = {
                // Agrega un nodo con un valor incremental basado en la longitud actual de la lista
                if (nodeList.size < initialList.size) {
                    nodeList.add(initialList[nodeList.size])
                    println("Nodo agregado: ${initialList[nodeList.size - 1]}")
                    println("Lista actual: $nodeList")
                } else {
                    println("No se pueden agregar más nodos. Lista completa.")
                }
            }) {
                Text("Agregar nodo")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                // Elimina el último nodo si la lista no está vacía
                if (nodeList.isNotEmpty()) {
                    val removedNode = nodeList.removeLast()
                    println("Nodo eliminado: $removedNode")
                    println("Lista actual: $nodeList")
                } else {
                    println("No hay nodos para eliminar.")
                }
            }) {
                Text("Eliminar nodo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visualización de los nodos de la lista doblemente enlazada en un Canvas
        NodeVisualizer(nodeList)
    }
}

@Composable
fun NodeVisualizer(nodeList: List<Double>) {
    // Obtenemos el ancho de la pantalla en píxeles
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    if (nodeList.isNotEmpty()) {
        val nodeRadius = 40f
        val nodeSpacing = 150f
        val rowHeight = 150f
        val startX = nodeRadius + 20f  // Margen desde el borde izquierdo
        val startY = nodeRadius + 20f  // Margen desde el borde superior

        // Calculamos la altura necesaria para el Canvas basado en el número de filas
        val nodesPerRow = ((screenWidthPx - startX * 2) / nodeSpacing).toInt()
        val numRows = (nodeList.size / nodesPerRow) + 1
        val canvasHeight = numRows * rowHeight

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(canvasHeight.dp)) {

            var xPosition = startX
            var yPosition = startY

            nodeList.forEachIndexed { index, value ->
                // Dibuja el nodo como un círculo sin relleno (solo contorno)
                drawCircle(
                    color = Color.Green,
                    radius = nodeRadius,
                    center = Offset(xPosition, yPosition),
                    style = Stroke(width = 4f)
                )

                // Dibuja el valor dentro del nodo
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        value.toString(),
                        xPosition - 20f,  // Ajusta la posición del texto
                        yPosition + 15f,
                        android.graphics.Paint().apply {
                            textSize = 40f
                            color = android.graphics.Color.BLACK
                        }
                    )
                }

                // Dibuja las flechas dobles hacia el siguiente nodo, si no es el último
                if (index < nodeList.size - 1) {
                    var nextXPosition = xPosition + nodeSpacing
                    var nextYPosition = yPosition

                    // Si el siguiente nodo se sale del ancho de la pantalla, saltamos a la siguiente línea
                    if (nextXPosition + nodeRadius > size.width) {
                        nextXPosition = startX
                        nextYPosition += rowHeight
                    }

                    // Dibuja la línea hacia el siguiente nodo
                    drawLine(
                        color = Color.Green,
                        start = Offset(xPosition + nodeRadius, yPosition),
                        end = Offset(nextXPosition - nodeRadius, nextYPosition),
                        strokeWidth = 4f
                    )

                    // Dibuja las flechas (doble sentido)
                    drawDoubleArrow(
                        start = Offset(xPosition + nodeRadius, yPosition),
                        end = Offset(nextXPosition - nodeRadius, nextYPosition)
                    )

                    // No actualizamos xPosition e yPosition aquí porque lo hacemos al inicio del loop
                }

                // Actualizamos las posiciones para el próximo nodo
                xPosition += nodeSpacing
                if (xPosition + nodeRadius > size.width) {
                    xPosition = startX
                    yPosition += rowHeight
                }
            }
        }
    } else {
        // Mensaje cuando la lista está vacía
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("La lista está vacía. Agrega un nodo.")
        }
    }
}

// Función para dibujar flechas dobles entre dos nodos
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDoubleArrow(start: Offset, end: Offset) {
    // Dibuja la línea principal
    drawLine(
        color = Color.Green,
        start = start,
        end = end,
        strokeWidth = 4f
    )

    // Calculamos el ángulo de la flecha
    val arrowAngle = atan2(end.y - start.y, end.x - start.x)
    val arrowLength = 20f

    // Flechas hacia adelante
    val arrowPoint1 = Offset(
        end.x - arrowLength * cos(arrowAngle - Math.PI.toFloat() / 6f),
        end.y - arrowLength * sin(arrowAngle - Math.PI.toFloat() / 6f)
    )
    val arrowPoint2 = Offset(
        end.x - arrowLength * cos(arrowAngle + Math.PI.toFloat() / 6f),
        end.y - arrowLength * sin(arrowAngle + Math.PI.toFloat() / 6f)
    )

    // Flechas hacia atrás
    val arrowPoint3 = Offset(
        start.x + arrowLength * cos(arrowAngle - Math.PI.toFloat() / 6f),
        start.y + arrowLength * sin(arrowAngle - Math.PI.toFloat() / 6f)
    )
    val arrowPoint4 = Offset(
        start.x + arrowLength * cos(arrowAngle + Math.PI.toFloat() / 6f),
        start.y + arrowLength * sin(arrowAngle + Math.PI.toFloat() / 6f)
    )

    // Dibuja las flechas (doble sentido)
    val arrowPath = Path().apply {
        // Flecha hacia adelante
        moveTo(end.x, end.y)
        lineTo(arrowPoint1.x, arrowPoint1.y)
        moveTo(end.x, end.y)
        lineTo(arrowPoint2.x, arrowPoint2.y)
        // Flecha hacia atrás
        moveTo(start.x, start.y)
        lineTo(arrowPoint3.x, arrowPoint3.y)
        moveTo(start.x, start.y)
        lineTo(arrowPoint4.x, arrowPoint4.y)
    }
    drawPath(path = arrowPath, color = Color.Green, style = Stroke(width = 4f))
}
