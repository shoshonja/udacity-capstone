package com.bikes.greyp.udacitycapstoneproject.usecases

import com.bikes.greyp.udacitycapstoneproject.data.database.RssDao
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFeedLocalUseCase(
    private val rssDao: RssDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getFeed(source: String): List<PartialFeedItem> = withContext(ioDispatcher) {
        return@withContext rssDao.getAllFeedsForGivenSource(source)
    }
}