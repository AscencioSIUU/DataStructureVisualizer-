package com.example.datastructurevisualizerapp.data.data_source

import com.example.datastructurevisualizerapp.data.BarData
import com.example.datastructurevisualizerapp.data.NormalizedBar
import kotlin.math.ceil


fun getBarData(start: Int = 1, end: Int = 100, samples: Int =  20): BarData {

    val rawData = List(samples) { (start..end).random().toFloat() }

    val max = rawData.maxOrNull() ?: 1

    val normalizedData = List(samples) { NormalizedBar(normalizedHeight = rawData[it]/max.toFloat(), label ="A${it}", initialIsSelected = false) }

    val marks = ceil(samples/3f).toInt()
    val scaleMarks = List(marks) { (it.toFloat() / marks) * max.toFloat() }
    return BarData(normalizedBars = normalizedData, scaleMarks = scaleMarks, heights = rawData)
}
