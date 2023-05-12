package com.bikes.greyp.udacitycapstoneproject.data.repository

import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.usecases.GetRidingSpotsUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.StoreRidingSpotUseCase

class RidingSpotRepository(
    private val getRidingSpotsUseCase: GetRidingSpotsUseCase,
    private val storeRidingSpotUseCase: StoreRidingSpotUseCase,
) {

    suspend fun getRidingSpots(): List<RidingSpot> =
        getRidingSpotsUseCase.getRidingSpots()

    suspend fun storeRidingSpot(ridingSpot: RidingSpot) {
        storeRidingSpotUseCase.storeRidingSpot(ridingSpot)
    }
}