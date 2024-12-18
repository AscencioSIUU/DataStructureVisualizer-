package com.example.datastructurevisualizerapp.data

import com.example.datastructurevisualizerapp.data.data_source.CoinGeckoApiService
import com.example.datastructurevisualizerapp.domain.models.Coin
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log

class CoinRepository{
    private val apiService = CoinGeckoApiService()
    suspend fun getAllCoins(limit: Int = 50): List<Coin>{
        val jsonResponse = apiService.fetchAllCoins()
        val coinList = mutableListOf<Coin>()

        if(jsonResponse != null){
            val jsonArray = JSONArray(jsonResponse)
            for (i in 0 until minOf(limit, jsonArray.length())){
                val coin = jsonArray.getJSONObject(i)
                val id = coin.getString("id")
                val name = coin.getString("name")
                val symbol = coin.getString("symbol")
                coinList.add(Coin(id, name, symbol))
            }
        }
        return coinList
    }

    suspend fun getCoinPrices(coins: List<Coin>): List<Coin>{
        val ids = coins.joinToString(",") { it.id }

        val jsonResponse = apiService.fetchCoinPrices(ids)
        if (jsonResponse != null) {
            val jsonObject = JSONObject(jsonResponse)
            for (coin in coins) {
                if (jsonObject.has(coin.id) && jsonObject.getJSONObject(coin.id).has("usd")) {
                    val coinData = jsonObject.getJSONObject(coin.id)
                    coin.price = coinData.getDouble("usd")
                }
            }
        }
        return coins
    }

    suspend fun getManualCoinPrices(coinNames: List<String>): List<Pair<String, Double>> {
        val names = coinNames.joinToString(",")
        val jsonResponse = apiService.fetchCoinPrices(names)
        val result = mutableListOf<Pair<String, Double>>()

        if (jsonResponse != null) {
            val jsonObject = JSONObject(jsonResponse)
            for (coinName in coinNames) {
                if (jsonObject.has(coinName) && jsonObject.getJSONObject(coinName).has("usd")) {
                    val coinData = jsonObject.getJSONObject(coinName)
                    val price = coinData.getDouble("usd")
                    result.add(Pair(coinName, price))  // Agrega el nombre y el precio a la lista
                }
            }
        }
        return result
    }

}