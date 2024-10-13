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
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun BinaryTreesVisualizer() {
    // Predefinido array de valores que se agregarán uno por uno
    val predefinedValues = listOf(23, 12, 45, 56, 86, 34, 56,45,34,23,12,45,67,78,76,54,67)

    // Lista de valores que efectivamente se están usando en el árbol
    var currentTree by remember { mutableStateOf<TreeNode?>(null) }
    var index by remember { mutableStateOf(0) }

    // Interfaz para agregar elementos al árbol
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    if (index < predefinedValues.size) {
                        val newValue = predefinedValues[index]
                        currentTree = insert(currentTree, newValue)  // Inserta el nuevo valor al árbol
                        index++  // Avanza el índice al siguiente valor
                        // Log para ver la estructura del árbol tras cada inserción
                        Log.d("BinaryTree", "Nodo agregado: $newValue")
                        printTree(currentTree)
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Agregar")
            }
        }

        // Si el árbol no está vacío, dibuja el árbol binario
        if (currentTree != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                BinaryTreeCanvas(treeNode = currentTree)
            }
        } else {
            // Mensaje para indicar que el árbol está vacío al inicio
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("El árbol está vacío. Agrega un valor.")
            }
        }
    }
}

@Composable
fun BinaryTreeCanvas(treeNode: TreeNode?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (treeNode != null) {
                // Dibuja el árbol con espaciado dinámico basado en el tamaño del árbol
                drawBinaryTree(treeNode, x = size.width / 2, y = 100f, level = 0, nodeSpacing = size.width / 2)
            }
        }
    }
}

// Función para dibujar el árbol binario usando `Canvas`
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBinaryTree(
    treeNode: TreeNode?,
    x: Float,
    y: Float,
    level: Int,
    nodeSpacing: Float,
    verticalSpacing: Float = 150f
) {
    if (treeNode != null) {
        // Dibuja el nodo
        drawCircle(
            color = Color.Green,
            radius = 40f,
            center = Offset(x, y),
            style = Stroke(width = 4f)
        )
        drawContext.canvas.nativeCanvas.drawText(
            treeNode.value.toString().padStart(4, '0'),
            x - 30f,
            y + 10f,
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 40f
            }
        )

        // Espaciado horizontal para los nodos hijos, ajustado dinámicamente
        val childSpacing = nodeSpacing / 2

        // Dibuja la conexión hacia el hijo izquierdo
        if (treeNode.left != null) {
            drawLine(
                color = Color.Green,
                start = Offset(x, y + 40f),
                end = Offset(x - childSpacing, y + verticalSpacing),
                strokeWidth = 4f
            )
            // Dibuja el subárbol izquierdo
            drawBinaryTree(
                treeNode.left,
                x = x - childSpacing,
                y = y + verticalSpacing,
                level = level + 1,
                nodeSpacing = childSpacing,
                verticalSpacing = verticalSpacing
            )
        }

        // Dibuja la conexión hacia el hijo derecho
        if (treeNode.right != null) {
            drawLine(
                color = Color.Green,
                start = Offset(x, y + 40f),
                end = Offset(x + childSpacing, y + verticalSpacing),
                strokeWidth = 4f
            )
            // Dibuja el subárbol derecho
            drawBinaryTree(
                treeNode.right,
                x = x + childSpacing,
                y = y + verticalSpacing,
                level = level + 1,
                nodeSpacing = childSpacing,
                verticalSpacing = verticalSpacing
            )
        }
    }
}

// Función para insertar valores en el árbol (árbol inmutable)
fun insert(root: TreeNode?, value: Int): TreeNode {
    if (root == null) {
        Log.d("BinaryTree", "Insertando nodo raíz: $value")
        return TreeNode(value)
    }
    return if (value < root.value) {
        Log.d("BinaryTree", "Insertando a la izquierda de ${root.value}: $value")
        root.copy(left = insert(root.left, value))
    } else {
        Log.d("BinaryTree", "Insertando a la derecha de ${root.value}: $value")
        root.copy(right = insert(root.right, value))
    }
}

// Función auxiliar para imprimir el árbol en los logs
fun printTree(node: TreeNode?, prefix: String = "", isLeft: Boolean = true) {
    if (node != null) {
        Log.d("BinaryTree", "$prefix${if (isLeft) "├── " else "└── "}${node.value}")
        printTree(node.left, prefix + if (isLeft) "│   " else "    ", true)
        printTree(node.right, prefix + if (isLeft) "│   " else "    ", false)
    }
}

// Estructura del nodo del árbol binario (inmutable)
data class TreeNode(
    val value: Int,
    val left: TreeNode? = null,
    val right: TreeNode? = null
)
