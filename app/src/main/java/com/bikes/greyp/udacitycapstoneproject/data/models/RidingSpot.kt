package com.bikes.greyp.udacitycapstoneproject.data.models

import java.io.Serializable
import java.util.UUID

data class RidingSpot(
    var title: String,
    var description: String = "",
    var location: String = "",
    var latitude: Double,
    var longitude: Double,
    val id: String = UUID.randomUUID().toString()
) : Serializable