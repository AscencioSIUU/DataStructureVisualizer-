package com.example.datastructurevisualizerapp.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.max
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

@Composable
fun BinaryTreesVisualizer(
    valuesList: List<Double>,
    onUseManualEntries: () -> List<Double>?,
    onUseCsvEntries: () -> List<Double>?
) {
    // Árbol binario actual
    var currentTree by remember { mutableStateOf<TreeNode?>(null) }
    var index by remember { mutableStateOf(0) }

    // Lista de valores actualmente en el árbol
    var treeValues by remember { mutableStateOf<List<Double>>(emptyList()) }

    // Estados para scroll horizontal y vertical
    val scrollStateVertical = rememberScrollState()
    val scrollStateHorizontal = rememberScrollState()

    // Estado para el menú desplegable
    var expanded by remember { mutableStateOf(false) }
    var selectedValueToDelete by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollStateVertical)
            .horizontalScroll(scrollStateHorizontal)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para agregar elementos desde `valuesList`
            Button(
                onClick = {
                    if (index < valuesList.size) {
                        val newValue = valuesList[index]
                        currentTree = insert(currentTree, newValue)  // Inserta el nuevo valor al árbol
                        index++  // Avanza el índice al siguiente valor
                        // Actualiza la lista de valores en el árbol
                        treeValues = getTreeValues(currentTree)
                        // Log para ver la estructura del árbol tras cada inserción
                        Log.d("BinaryTree", "Nodo agregado: $newValue")
                        printTree(currentTree)
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Agregar")
            }

            // Botón para utilizar los datos ingresados manualmente
            Button(
                onClick = {
                    val manualEntries = onUseManualEntries() // Llama la función para obtener los datos ingresados manualmente
                    if (manualEntries != null && manualEntries.isNotEmpty()) {
                        manualEntries.forEach { value ->
                            currentTree = insert(currentTree, value)  // Inserta cada valor en el árbol
                        }
                        // Actualiza la lista de valores en el árbol
                        treeValues = getTreeValues(currentTree)
                        Log.d("BinaryTree", "Datos manuales agregados al árbol: $manualEntries")
                        printTree(currentTree)
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Utilizar los ingresados")
            }

            // **Botón para utilizar los datos del CSV**
            Button(
                onClick = {
                    val csvEntries = onUseCsvEntries() // Llama la función para obtener los datos del CSV
                    if (csvEntries != null && csvEntries.isNotEmpty()) {
                        csvEntries.forEach { value ->
                            currentTree = insert(currentTree, value)  // Inserta cada valor en el árbol
                        }
                        // Actualiza la lista de valores en el árbol
                        treeValues = getTreeValues(currentTree)
                        Log.d("BinaryTree", "Datos CSV agregados al árbol: $csvEntries")
                        printTree(currentTree)
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Utilizar CSV")
            }

            // Menú desplegable para seleccionar el valor a eliminar
            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                Button(
                    onClick = { expanded = true },
                    enabled = treeValues.isNotEmpty()
                ) {
                    Text(text = selectedValueToDelete?.toString() ?: "Seleccione valor")
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    treeValues.forEach { value ->
                        DropdownMenuItem(
                            text = { Text(text = value.toString()) },
                            onClick = {
                                selectedValueToDelete = value
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Botón para eliminar el valor seleccionado
            Button(
                onClick = {
                    if (selectedValueToDelete != null) {
                        currentTree = delete(currentTree, selectedValueToDelete!!)
                        // Actualiza la lista de valores en el árbol
                        treeValues = getTreeValues(currentTree)
                        // Log para ver la estructura del árbol tras la eliminación
                        Log.d("BinaryTree", "Nodo eliminado: $selectedValueToDelete")
                        printTree(currentTree)
                        // Reinicia el valor seleccionado
                        selectedValueToDelete = null
                    }
                },
                enabled = selectedValueToDelete != null,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Eliminar")
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
            // Mensaje para indicar que el árbol está vacío
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
    // Calculamos el tamaño necesario para el Canvas
    val treeHeight = getTreeHeight(treeNode)
    val canvasHeight = treeHeight * 150f + 200f  // Ajusta según el espaciado vertical
    val canvasWidth = getTreeWidth(treeNode) * 100f + 200f  // Ajusta según el espaciado horizontal

    Box(
        modifier = Modifier
            .width(canvasWidth.dp)
            .height(canvasHeight.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (treeNode != null) {
                // Dibuja el árbol con espaciado dinámico basado en el tamaño del árbol
                drawBinaryTree(treeNode, x = size.width / 2, y = 100f, level = 0, nodeSpacing = size.width / 4)
            }
        }
    }
}

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
        val childSpacing = nodeSpacing / 1.5f

        // Dibuja la conexión hacia el hijo izquierdo
        if (treeNode.left != null) {
            drawLine(
                color = Color.Green,
                start = Offset(x, y + 40f),
                end = Offset(x - childSpacing, y + verticalSpacing),
                strokeWidth = 4f
            )
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

fun getTreeHeight(node: TreeNode?): Int {
    if (node == null) return 0
    return 1 + max(getTreeHeight(node.left), getTreeHeight(node.right))
}

fun getTreeWidth(node: TreeNode?): Int {
    if (node == null) return 0
    if (node.left == null && node.right == null) return 1
    return getTreeWidth(node.left) + getTreeWidth(node.right)
}

fun insert(root: TreeNode?, value: Double): TreeNode {
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

fun delete(root: TreeNode?, value: Double): TreeNode? {
    if (root == null) {
        return null
    }
    return when {
        value < root.value -> {
            root.copy(left = delete(root.left, value))
        }
        value > root.value -> {
            root.copy(right = delete(root.right, value))
        }
        else -> { // value == root.value
            if (root.left == null && root.right == null) {
                null
            } else if (root.left == null) {
                root.right
            } else if (root.right == null) {
                root.left
            } else {
                val successorValue = findMinValue(root.right)
                root.copy(
                    value = successorValue,
                    right = delete(root.right, successorValue)
                )
            }
        }
    }
}

fun findMinValue(node: TreeNode?): Double {
    return node?.left?.let { findMinValue(it) } ?: node!!.value
}

fun getTreeValues(node: TreeNode?): List<Double> {
    if (node == null) return emptyList()
    val leftValues = getTreeValues(node.left)
    val rightValues = getTreeValues(node.right)
    return leftValues + node.value + rightValues
}

fun printTree(node: TreeNode?, prefix: String = "", isLeft: Boolean = true) {
    if (node != null) {
        Log.d("BinaryTree", "$prefix${if (isLeft) "├── " else "└── "}${node.value}")
        printTree(node.left, prefix + if (isLeft) "│   " else "    ", true)
        printTree(node.right, prefix + if (isLeft) "│   " else "    ", false)
    }
}

data class TreeNode(
    val value: Double,
    val left: TreeNode? = null,
    val right: TreeNode? = null
)
