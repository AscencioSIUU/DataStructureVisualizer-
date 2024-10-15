package com.example.datastructurevisualizerapp.algorithmLogic

import com.example.datastructurevisualizerapp.data.data_source.NormalizedBar


interface SortingStrategy{
    suspend fun sort(normalizedList: MutableList<NormalizedBar>)
}