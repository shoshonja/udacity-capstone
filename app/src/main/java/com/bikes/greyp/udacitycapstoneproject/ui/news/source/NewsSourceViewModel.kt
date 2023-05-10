package com.bikes.greyp.udacitycapstoneproject.ui.news.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.usecases.CheckConnectionUseCase
import kotlinx.coroutines.launch

class NewsSourceViewModel(private val checkConnectionUseCase: CheckConnectionUseCase) :
    ViewModel() {

    val internetConnection: LiveData<Boolean>
        get() = _internetConnection

    private val _internetConnection = MutableLiveData<Boolean>()

    val rssSource: LiveData<RssSource>
        get() = _rssSource

    private val _rssSource = MutableLiveData<RssSource>()

    fun checkConnectionAndGetFeeds(rssSource: RssSource) {
        viewModelScope.launch {
            val isConnected = isConnectedToInternet()
            if (isConnected) {
                _rssSource.value = rssSource
            } else {
                _internetConnection.value = false
            }
        }
    }

    private suspend fun isConnectedToInternet() = checkConnectionUseCase.checkNetwork()
}