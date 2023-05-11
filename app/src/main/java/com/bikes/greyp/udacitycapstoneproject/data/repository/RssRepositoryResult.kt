package com.bikes.greyp.udacitycapstoneproject.data.repository

import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem

interface RssRepositoryResult {
    fun onResult(result: List<PartialFeedItem>)

    fun onError(throwable: Throwable)
}