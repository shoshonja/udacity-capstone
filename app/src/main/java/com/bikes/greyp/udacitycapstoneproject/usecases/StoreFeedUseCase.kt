package com.bikes.greyp.udacitycapstoneproject.usecases

import com.bikes.greyp.udacitycapstoneproject.data.database.RssDao
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoreFeedUseCase(
    private val rssDao: RssDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun storeFeed(fetchedFeed: List<PartialFeedItem>) =
        withContext(ioDispatcher) {
            for (feed in fetchedFeed) {
                rssDao.insert(feed)
            }
        }
}