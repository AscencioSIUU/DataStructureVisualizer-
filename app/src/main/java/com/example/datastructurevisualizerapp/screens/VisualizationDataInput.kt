package com.example.datastructurevisualizerapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datastructurevisualizerapp.components.visualizationDataComponents.ChipSelectorState
import com.example.datastructurevisualizerapp.components.visualizationDataComponents.ChipsSelector

@Composable
fun VisualizationDataInputScreen(modifier: Modifier = Modifier, chipSelectorState: ChipSelectorState) {

    ChipsSelector(state = chipSelectorState)
}