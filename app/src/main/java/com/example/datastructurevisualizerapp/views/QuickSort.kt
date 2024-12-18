package com.example.datastructurevisualizerapp.views

import androidx.compose.runtime.Composable
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel

@Composable
fun QuickSortVisualizer(barGraphViewModel: BarGraphViewModel){
    AlgorithmScreen(barGraphViewModel = barGraphViewModel, sortingEnum = SortingEnum.QUICK)
}