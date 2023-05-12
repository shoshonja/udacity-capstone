package com.bikes.greyp.udacitycapstoneproject.ui.parkmap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.data.repository.RidingSpotRepository
import kotlinx.coroutines.launch

class ParkMapViewModel(private val ridingSpotRepository: RidingSpotRepository) : ViewModel() {
    val ridingSpots: LiveData<List<RidingSpot>>
        get() = _ridingSpots

    private val _ridingSpots = MutableLiveData<List<RidingSpot>>()


    fun getRidingSpots() {
        viewModelScope.launch {
            _ridingSpots.value = ridingSpotRepository.getAllRidingSpots()
        }

        val leogang = RidingSpot(
            title = "Leogang",
            latitude = 47.44005032771997,
            longitude = 12.719990909633754
        )
        val fortWilliam = RidingSpot(
            title = "Fort William",
            latitude = 56.8206887856682,
            longitude = -5.104950651034826
        )
        val whistler = RidingSpot(
            title = "Whistler",
            latitude = 50.117349530567296,
            longitude = -122.95158610313065
        )
        val maribor = RidingSpot(
            title = "Maribor",
            latitude = 46.53333743521733,
            longitude = 15.599602102855085
        )

        _ridingSpots.value = listOf(leogang, fortWilliam, whistler, maribor)

    }
}