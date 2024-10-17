package com.example.datastructurevisualizerapp.domain.models

import kotlinx.coroutines.flow.Flow

class OfflineCoinRepository(private val coinDao: CoinDao) : OfflineCoinRepoIn {
    override fun getAllCoinsStream(): Flow<List<Coin>> = coinDao.getAllCoins()

    override fun getCoinStream(id: String): Flow<Coin?> = coinDao.getCoin(id)

    override fun getCoinPricesStream(): Flow<List<Double>> = coinDao.getAllCoinPrices()

    override suspend fun deleteAllDbCoins() = coinDao.deleteAllCoins()

    override suspend fun insertItem(item: Coin) = coinDao.insert(item)

    override suspend fun deleteItem(item: Coin) = coinDao.delete(item)

    override suspend fun updateItem(item: Coin) = coinDao.update(item)
}