package com.example.datastructurevisualizerapp.algorithmLogic

import com.example.datastructurevisualizerapp.data.NormalizedBar


class SortingContext(){
    private var _sortingStrategy: SortingStrategy = BubbleSortStrategy()

    fun setSortingStrategy(sortingStrategy: SortingStrategy){
        _sortingStrategy = sortingStrategy
    }

    suspend fun executeSortStrategy(normalizedList: MutableList<NormalizedBar>){
        _sortingStrategy.sort(normalizedList)
    }
}