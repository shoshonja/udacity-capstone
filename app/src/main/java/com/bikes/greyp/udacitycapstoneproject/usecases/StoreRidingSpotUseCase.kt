package com.bikes.greyp.udacitycapstoneproject.usecases

import com.bikes.greyp.udacitycapstoneproject.data.database.RidingSpotDao
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoreRidingSpotUseCase(
    private val ridingSpotDao: RidingSpotDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun storeRidingSpot(ridingSpot: RidingSpot) =
       withContext(ioDispatcher){
           ridingSpotDao.insert(ridingSpot)
       }
}