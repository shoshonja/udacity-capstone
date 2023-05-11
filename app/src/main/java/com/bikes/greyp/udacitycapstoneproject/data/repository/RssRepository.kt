package com.bikes.greyp.udacitycapstoneproject.data.repository

import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.usecases.CheckConnectionUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.GetFeedLocalUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.GetFeedRemoteUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.StoreFeedUseCase
import com.bikes.greyp.udacitycapstoneproject.utils.Parser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RssRepository(
    private val parser: Parser,
    private val checkConnectionUseCase: CheckConnectionUseCase,
    private val getFeedLocalUseCase: GetFeedLocalUseCase,
    private val getFeedRemoteUseCase: GetFeedRemoteUseCase,
    private val storeFeedUseCase: StoreFeedUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : Repository {

    private lateinit var resultListener: RssRepositoryResult

    override suspend fun getFeeds(feedUrl: String, resultListener: RssRepositoryResult) {
        this.resultListener = resultListener
        checkConnectionAndGetFeeds(feedUrl)
    }

    private suspend fun checkConnectionAndGetFeeds(feedUrl: String) = with(ioDispatcher) {
        val isConnected = isConnectedToInternet()
        if (isConnected) {
            getRemoteFeeds(feedUrl)
        } else {
            getLocalFeeds(feedUrl)
        }
    }

    private suspend fun isConnectedToInternet() = checkConnectionUseCase.checkNetwork()

    private suspend fun getRemoteFeeds(feedUrl: String) =
        getFeedRemoteUseCase.getFeed(feedUrl, createRssFeedCallback(feedUrl))

    private suspend fun getLocalFeeds(feedUrl: String) {
        getFeedLocalUseCase.getFeed(parser.extractSource(feedUrl))
    }

    private suspend fun createRssFeedCallback(feedUrl: String): Callback<String> {
        return object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val retrievedFeeds = response.body()
                if (retrievedFeeds == null) {
                    resultListener.onError(Throwable("Body not retrieved"))
                    return
                }

                try {
                    val partialFeedItems = parser.createPartialFeedItems(
                        parser.extractSource(feedUrl),
                        parser.extractItems(retrievedFeeds)
                    )
                    CoroutineScope(ioDispatcher).launch {
                        storeFeed(partialFeedItems)
                    }
                    resultListener.onResult(partialFeedItems)
                } catch (e: Exception) {
                    resultListener.onError(e)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                resultListener.onError(t)
            }
        }
    }

    private suspend fun storeFeed(partialFeedItemList: List<PartialFeedItem>) =
        withContext(ioDispatcher) {
            try {
                storeFeedUseCase.storeFeed(partialFeedItemList)
            } catch (throwable: Throwable) {
                resultListener.onError(throwable)
            }
        }
}