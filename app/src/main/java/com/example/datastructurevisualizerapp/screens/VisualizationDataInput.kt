package com.example.datastructurevisualizerapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datastructurevisualizerapp.components.visualizationDataComponents.ChipSelectorState
import com.example.datastructurevisualizerapp.components.visualizationDataComponents.ChipsSelector
import com.google.protobuf.Internal.BooleanList

@Composable
fun VisualizationDataInputScreen(modifier: Modifier = Modifier, randomClick: () -> Boolean, cryptoClick: () -> Boolean, manualClick: () -> Boolean, csvClick: () -> Boolean, selectedChip: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        ChipsSelector(
            randomClick = randomClick,
            cryptoClick = cryptoClick,
            manualClick = manualClick,
            csvClick = csvClick,
            cselectedChip = selectedChip
        )

    }

}


