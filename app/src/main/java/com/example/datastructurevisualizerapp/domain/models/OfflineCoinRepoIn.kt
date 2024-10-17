package com.example.datastructurevisualizerapp.domain.models

import kotlinx.coroutines.flow.Flow

interface OfflineCoinRepoIn {
    fun getAllCoinsStream(): Flow<List<Coin>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getCoinStream(id: String): Flow<Coin?>

    fun getCoinPricesStream(): Flow<List<Double>>

    suspend fun deleteAllDbCoins()

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Coin)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Coin)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Coin)
}