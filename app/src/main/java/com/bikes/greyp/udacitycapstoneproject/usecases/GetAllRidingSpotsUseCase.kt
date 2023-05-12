package com.bikes.greyp.udacitycapstoneproject.usecases

import com.bikes.greyp.udacitycapstoneproject.data.database.RidingSpotDao
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllRidingSpotsUseCase(
    private val ridingSpotDao: RidingSpotDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getRidingSpots(): List<RidingSpot> =
        withContext(ioDispatcher) {
            return@withContext ridingSpotDao.getAllRidingSpots()
        }
}