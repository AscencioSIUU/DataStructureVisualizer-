package com.example.datastructurevisualizerapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NormalizedBar(
    val normalizedHeight: Float,
    val label: String,
    initialIsSelected: Boolean
){
    var selected by mutableStateOf(initialIsSelected)
}

data class BarData(val normalizedBars: List<NormalizedBar>, val scaleMarks: List<Float>, val heights: List<Float>)
