package com.example.datastructurevisualizerapp.data.data_source

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