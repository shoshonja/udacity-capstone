package com.bikes.greyp.udacitycapstoneproject.ui.parkmap.addridingspot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.data.repository.RidingSpotRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class AddRidingSpotViewModel(private val ridingSpotRepository: RidingSpotRepository) : ViewModel() {

    val ridingSpotSaved: LiveData<Boolean>
        get() = _ridingSpotSaved

    private val _ridingSpotSaved = MutableLiveData<Boolean>()

    fun saveRidingSpot(
        title: String,
        location: String = "",
        description: String = "",
        position: LatLng
    ) {
        viewModelScope.launch {
            try {
                ridingSpotRepository.storeRidingSpot(
                    RidingSpot(
                        title = title,
                        description = description,
                        location = location,
                        latitude = position.latitude,
                        longitude = position.longitude
                    )
                )
            } catch (throwable: Throwable){
                _ridingSpotSaved.value = false
            } finally {
                _ridingSpotSaved.value = true
            }
        }
    }
}