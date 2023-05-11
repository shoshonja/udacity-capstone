package com.bikes.greyp.udacitycapstoneproject.data.repository

interface Repository {

    suspend fun getFeeds(feedUrl: String, resultListener: RssRepositoryResult)
}