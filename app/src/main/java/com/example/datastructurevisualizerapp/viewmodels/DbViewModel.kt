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
import kotlinx.coroutines.flow.first

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

    
}

data class AllCoins(val coinList: List<Coin> = listOf())

data class AllPrices(val priceList: List<Double> = listOf())