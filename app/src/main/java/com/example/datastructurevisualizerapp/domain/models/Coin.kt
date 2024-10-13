package com.example.datastructurevisualizerapp.domain.models

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    var price: Double? = null
)

