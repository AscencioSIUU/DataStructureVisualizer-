package com.example.datastructurevisualizerapp.viewmodels

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.datastructurevisualizerapp.algorithmLogic.BubbleSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.InsertionSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.SelectionSortStrategy
import com.example.datastructurevisualizerapp.algorithmLogic.SortingContext
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.data.NormalizedBar
import com.example.datastructurevisualizerapp.data.data_source.getBarData
import kotlinx.coroutines.launch

class BarGraphViewModel(normalizedBar: List<NormalizedBar>, scaleMarks: List<Float>): ViewModel(){
    private var _normalizedBars = normalizedBar.toMutableStateList()
    var scaleMarks = scaleMarks
    private val sortingContext = SortingContext()
    val normalizedBars: List<NormalizedBar>
        get() = _normalizedBars

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

    fun resetData(){
        viewModelScope.launch {
            val barData = getBarData()

            _normalizedBars.clear()
            _normalizedBars.addAll(barData.normalizedBars)

            scaleMarks = barData.scaleMarks
        }

    }

}


