package com.example.datastructurevisualizerapp.algorithmLogic

import com.example.datastructurevisualizerapp.data.NormalizedBar


interface SortingStrategy{
    suspend fun sort(normalizedList: MutableList<NormalizedBar>)
}