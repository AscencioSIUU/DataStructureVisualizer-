package com.example.datastructurevisualizerapp.domain.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: Coin)

    @Update
    suspend fun update(coin: Coin)

    @Delete
    suspend fun delete(coin: Coin)

    @Query("SELECT * from coins WHERE id = :id")
    fun getCoin(id: String): Flow<Coin>

    @Query("SELECT * from coins")
    fun getAllCoins(): Flow<List<Coin>>

    @Query("SELECT price from coins")
    fun getAllCoinPrices(): Flow<List<Double>>

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()
}