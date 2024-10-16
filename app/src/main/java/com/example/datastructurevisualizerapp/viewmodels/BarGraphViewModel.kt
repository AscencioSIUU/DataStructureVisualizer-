package com.example.datastructurevisualizerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastructurevisualizerapp.algorithmLogic.BubbleSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.InsertionSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.SelectionSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.SortingContext
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.data.NormalizedBar
import kotlinx.coroutines.launch

class BarGraphViewModel(normalizedBar: List<NormalizedBar>, heights: List<Float>): ViewModel(){
    private var _normalizedBars = normalizedBar.toMutableList()
    val heights = heights
    private val sortingContext = SortingContext()
    val normalizedBars: List<NormalizedBar>
        get() = _normalizedBars



    fun setListItem(index: Int, item: NormalizedBar){
        _normalizedBars[index] = item
    }

    fun setSortingStrategy(strategyEnum: SortingEnum){
        when(strategyEnum){
            SortingEnum.BUBBLE -> sortingContext.setSortingStrategy(BubbleSortStrategy())
            SortingEnum.INSERTION -> sortingContext.setSortingStrategy(InsertionSortStrategy())
            SortingEnum.SELECTION -> sortingContext.setSortingStrategy(SelectionSortStrategy())
        }
    }

    fun sort(){
        viewModelScope.launch {
            sortingContext.executeSortStrategy(_normalizedBars)
        }
    }


}


