package com.bikes.greyp.udacitycapstoneproject.data.repository

import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.usecases.GetAllRidingSpotsUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.GetRidingSpotUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.StoreRidingSpotUseCase

class RidingSpotRepository(
    private val getRidingSpotUseCase: GetRidingSpotUseCase,
    private val getAllRidingSpotsUseCase: GetAllRidingSpotsUseCase,
    private val storeRidingSpotUseCase: StoreRidingSpotUseCase,
) {

    suspend fun getRidingSpot(spotId: String): RidingSpot? =
        getRidingSpotUseCase.getRidingSpot(spotId)

    suspend fun getAllRidingSpots(): List<RidingSpot> =
        getAllRidingSpotsUseCase.getRidingSpots()

    suspend fun storeRidingSpot(ridingSpot: RidingSpot) {
        storeRidingSpotUseCase.storeRidingSpot(ridingSpot)
    }
}