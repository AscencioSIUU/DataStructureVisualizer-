package com.example.datastructurevisualizerapp.views

import androidx.compose.runtime.Composable
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel

@Composable
fun MergeSortVisualizer(barGraphViewModel: BarGraphViewModel){
    barGraphViewModel.resetData()
    AlgorithmScreen(barGraphViewModel = barGraphViewModel, sortingEnum = SortingEnum.MERGE)
}