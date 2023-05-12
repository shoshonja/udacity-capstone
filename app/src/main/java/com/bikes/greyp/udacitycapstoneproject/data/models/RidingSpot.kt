package com.bikes.greyp.udacitycapstoneproject.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "local_riding_spot_storage")
data class
RidingSpot(

    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "riding_spot_title")
    var title: String,

    @ColumnInfo(name = "riding_spot_description")
    var description: String = "",

    @ColumnInfo(name = "riding_spot_location")
    var location: String = "",

    @ColumnInfo(name = "riding_spot_latitude")
    var latitude: Double,

    @ColumnInfo(name = "riding_spot_longitude")
    var longitude: Double,
)