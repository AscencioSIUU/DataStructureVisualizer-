package com.example.datastructurevisualizerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastructurevisualizerapp.data.BarData
import com.example.datastructurevisualizerapp.data.NormalizedBar
import com.example.datastructurevisualizerapp.domain.models.Coin
import com.example.datastructurevisualizerapp.domain.models.OfflineCoinRepoIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DbViewModel(private val coinRepository: OfflineCoinRepoIn): ViewModel() {


    var allCoins: StateFlow<AllCoins> = coinRepository.getAllCoinsStream().map { AllCoins(it)}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue =  AllCoins()
        )

    var allPrices: StateFlow<AllPrices> = coinRepository.getCoinPricesStream().map { AllPrices(it)}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue =  AllPrices()
        )



    suspend fun insertAllCoins(coins: List<Coin>){
        for(coin in coins){
            coinRepository.insertItem(coin)
        }
    }

    suspend fun clearDb(){
        coinRepository.deleteAllDbCoins()
    }

    @Composable
    fun getCoinDataBar(samples: Int, marks: Int = 5): BarData{
        Log.d("DbViewModel", "${coinRepository.getCoinPricesStream()}}")

        val myCoins = allCoins.collectAsState()
        val myPrices = allPrices.collectAsState()

        val pricesList: MutableList<Double> = myPrices.value.priceList.toMutableList()

        val coinList: MutableList<Coin> = myCoins.value.coinList.toMutableList()

        val max = pricesList.maxOrNull() ?: 1
        var normalizedData = emptyList<NormalizedBar>()
        var scaleMarks = emptyList<Float>()
        if(pricesList.isNotEmpty() and coinList.isNotEmpty()){
            normalizedData = List(samples) { NormalizedBar(normalizedHeight = (pricesList[it]/max.toDouble()).toFloat(), label = coinList[it].symbol, initialIsSelected = false) }


            scaleMarks = List(marks) { (it.toFloat() / marks) * max.toFloat() }
        }


        return BarData(normalizedBars = normalizedData, scaleMarks = scaleMarks)

    }

    @Composable
    fun getCoinPrices(): List<Double>{
        val myPrices = allPrices.collectAsState()

        val prices: MutableList<Double> = myPrices.value.priceList.toMutableList()

        return prices
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun processCsvNumbers(csvContent: String) {
        // Convertir el contenido del CSV en una lista de números
        val numberList = csvContent.lines()
            .filter { it.isNotBlank() }
            .flatMap { line ->
                line.split(",").mapNotNull { it.trim().toIntOrNull() }
            }

        // Aquí puedes actualizar una lista local o usar StateFlow para que sea observable
        viewModelScope.launch {
            // Aquí podrías usar un `MutableStateFlow` para que el UI pueda observar los cambios
            _storedNumbers.value = numberList
        }
    }


    // Un MutableStateFlow para almacenar los números y observar cambios
    private val _storedNumbers = MutableStateFlow<List<Int>>(emptyList())
    val storedNumbers: StateFlow<List<Int>> = _storedNumbers

    fun updateStoredNumbers(numbers: List<Int>) {
        _storedNumbers.value = numbers
    }
}

data class AllCoins(val coinList: List<Coin> = listOf())

data class AllPrices(val priceList: List<Double> = listOf())