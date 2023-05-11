package com.bikes.greyp.udacitycapstoneproject.ui.news.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.data.repository.Repository
import com.bikes.greyp.udacitycapstoneproject.data.repository.RssRepositoryResult
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val repository: Repository
) : ViewModel() {

    val retrievedRssFeeds: LiveData<List<PartialFeedItem>>
        get() = _retrievedRssFeeds

    private val _retrievedRssFeeds = MutableLiveData<List<PartialFeedItem>>()

    val retrievedRssFeedError: LiveData<Throwable>
        get() = _retrievedRssFeedError

    private val _retrievedRssFeedError = MutableLiveData<Throwable>()

    fun getFeeds(rssSource: RssSource) {
        viewModelScope.launch {
            repository.getFeeds(rssSource.url, createRepositoryResultListener())
        }
    }

    private fun createRepositoryResultListener(): RssRepositoryResult {
        return object : RssRepositoryResult {
            override fun onResult(result: List<PartialFeedItem>) {
                _retrievedRssFeeds.value = result
            }

            override fun onError(throwable: Throwable) {
                _retrievedRssFeedError.value = throwable
            }
        }
    }
}