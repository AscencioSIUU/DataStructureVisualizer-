package com.example.datastructurevisualizerapp.views

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel

@Composable
fun BubbleSortVisualizer(barGraphViewModel: BarGraphViewModel){
    barGraphViewModel.resetData()
    AlgorithmScreen(barGraphViewModel = barGraphViewModel, sortingEnum = SortingEnum.BUBBLE, modifier = Modifier.padding(100.dp))
}