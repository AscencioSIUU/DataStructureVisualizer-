package com.example.datastructurevisualizerapp.domain.models

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: OfflineCoinRepoIn
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: OfflineCoinRepoIn by lazy {
        OfflineCoinRepository(CoinDatabase.getDatabase(context).coinDao())
    }
}