package com.bikes.greyp.udacitycapstoneproject.usecases

import com.bikes.greyp.udacitycapstoneproject.data.network.RssNetworkApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Callback

class GetFeedRemoteUseCase(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getFeed(feedUrl: String, callback: Callback<String>) = withContext(ioDispatcher) {
        RssNetworkApi.retrofitApiService.getRssFeed(feedUrl).enqueue(callback)
    }
}