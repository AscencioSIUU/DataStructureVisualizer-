package com.example.datastructurevisualizerapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.datastructurevisualizerapp.viewmodels.DbViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel

        // Initializer for HomeViewModel
        initializer {
            DbViewModel(inventoryApplication().container.itemsRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): DataStructureVisualizerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DataStructureVisualizerApplication)
