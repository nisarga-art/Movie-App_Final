package com.example.movieapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager // New import
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    // --- THIS IS THE OTHER HALF OF THE FIX ---
    override fun onCreate() {
        super.onCreate()
        // Manually initialize WorkManager after Hilt is ready
        WorkManager.getInstance(this)
    }
}