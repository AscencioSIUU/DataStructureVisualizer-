package com.example.datastructurevisualizerapp.domain.models


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Coin::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    companion object {
        @Volatile
        private var Instance: CoinDatabase? = null

        fun getDatabase(context: Context): CoinDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CoinDatabase::class.java, "coin_database")
                    .build().also { Instance = it }
            }
        }
    }
}