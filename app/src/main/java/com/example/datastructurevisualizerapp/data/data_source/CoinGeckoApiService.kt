package com.example.datastructurevisualizerapp.data.data_source

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws


class CoinGeckoApiService {
    private val client = OkHttpClient()

    @Throws(IOException::class)
    suspend fun fetchAllCoins() : String? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://api.coingecko.com/api/v3/coins/list")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-jyWUfskUV5tkGyMQS4yxbdxr\t")
                .build()
            val response: Response = client.newCall(request).execute()
            response.body?.string()
        }
    }

    @Throws(IOException::class)
    suspend fun fetchCoinPrices(ids: String): String? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://api.coingecko.com/api/v3/simple/price?ids=$ids&vs_currencies=usd")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-jyWUfskUV5tkGyMQS4yxbdxr\t")
                .build()
            val response: Response = client.newCall(request).execute()
            response.body?.string()
        }
    }
}