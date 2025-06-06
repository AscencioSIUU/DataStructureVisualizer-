package com.example.datastructurevisualizerapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import com.example.datastructurevisualizerapp.components.homeComponents.dataStructureBox
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.R

@Composable
fun homeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Permitir que la pantalla cambie de orientación
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(dataStructuresCollection.size) { index ->
            dataStructureBox(
                drawable = dataStructuresCollection[index].drawable,
                text = dataStructuresCollection[index].text,
                navController = navController
            )
        }
    }
}

private val dataStructuresCollection = listOf(
    R.drawable.mergeicon to R.string.data_structure_merge,
    R.drawable.bubbleicon to R.string.data_structure_bubble,
    R.drawable.selectionicon to R.string.data_structure_selection,
    R.drawable.insertionicon to R.string.data_structure_insertion,
    R.drawable.quicksorticon to R.string.data_structure_quick,
    R.drawable.stackicon to R.string.data_structure_stacks,
    R.drawable.queuesicon to R.string.data_structure_queues,
    R.drawable.binarytreesicon to R.string.data_structure_binary_trees,
    R.drawable.heapsicon to R.string.data_structure_heaps,
    R.drawable.doublelinkedlistsicon to R.string.data_structure_double_linked_list
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)