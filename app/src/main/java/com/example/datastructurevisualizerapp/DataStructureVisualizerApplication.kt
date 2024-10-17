package com.example.datastructurevisualizerapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.datastructurevisualizerapp.domain.models.AppContainer
import com.example.datastructurevisualizerapp.domain.models.AppDataContainer

class DataStructureVisualizerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
