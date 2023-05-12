package com.bikes.greyp.udacitycapstoneproject.data.repository

import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot

interface RidingSpotRepositoryResult {
    fun onResult(result: List<RidingSpot>)

    fun onError(throwable: Throwable)
}