package com.example.datastructurevisualizerapp.data.data_source

import com.example.datastructurevisualizerapp.views.NormalizedBar

fun getNormalizedList(start: Int = 1, end: Int = 100, samples: Int = 10): List<NormalizedBar> {

    val rawData = List(samples) { (start..end).random() }

    val max = rawData.maxOrNull() ?: 1

    return List(samples) { NormalizedBar(normalizedHeight = rawData[it]/max.toFloat(), label ="", initialIsSelected = false) }

}