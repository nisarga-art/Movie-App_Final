package com.example.movieapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log // Import Log for debugging
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivity @Inject constructor(@ApplicationContext private val context: Context) {

    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network == null) {
            // No active network
            Log.d("NetworkConnectivity", "isOnline: false (No active network)")
            return false
        }

        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities == null) {
            // Network capabilities could not be determined
            Log.d("NetworkConnectivity", "isOnline: false (No network capabilities)")
            return false
        }

        // Check for an actual internet connection, not just a network type
        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        Log.d("NetworkConnectivity", "isOnline: $isConnected")
        return isConnected
    }
}