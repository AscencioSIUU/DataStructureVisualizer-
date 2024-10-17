package com.example.datastructurevisualizerapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "coins")
data class Coin(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    var price: Double = 0.0
)

