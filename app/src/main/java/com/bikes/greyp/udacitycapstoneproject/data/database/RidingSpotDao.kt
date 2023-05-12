package com.bikes.greyp.udacitycapstoneproject.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot

@Dao
interface RidingSpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ridingSpot: RidingSpot)

    @Query("SELECT * FROM local_riding_spot_storage")
    fun getAllRidingSpots(): List<RidingSpot>

    @Query("SELECT * FROM local_riding_spot_storage WHERE id = :spotId")
    fun getRidingSpot(spotId: String): RidingSpot?
}