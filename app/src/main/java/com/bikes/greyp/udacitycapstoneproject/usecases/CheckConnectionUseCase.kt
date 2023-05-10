package com.bikes.greyp.udacitycapstoneproject.usecases

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckConnectionUseCase(
    private val application: Application,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun checkNetwork(): Boolean = withContext(ioDispatcher) {
        val connectionManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.allNetworkInfo

        for (network in networkInfo) {
            if (network != null && network.isConnected) {
                return@withContext true
            }
        }
        return@withContext false
    }
}