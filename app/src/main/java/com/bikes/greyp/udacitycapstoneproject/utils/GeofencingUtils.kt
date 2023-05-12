package com.bikes.greyp.udacitycapstoneproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.device.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

const val GEOFENCE_RADIUS_METERS = 100f
const val GEOFENCE_DURATION_MILLIS = 3_600_000L
const val ACTION_GEOFENCE_EVENT =
    "com.bikes.greyp.udacitycapstoneproject.action.ACTION_GEOFENCE_EVENT"

@SuppressLint("MissingPermission")
fun createGeofence(activity: Activity, ridingSpot: RidingSpot) {
    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(
            activity,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    val geofencingClient = LocationServices.getGeofencingClient(activity)

    geofencingClient.removeGeofences(geofencePendingIntent)?.run {
        addOnCompleteListener {
            geofencingClient.addGeofences(
                createGeofencingRequest(ridingSpot),
                geofencePendingIntent
            )
        }
    }
}

fun createGeofencingRequest(ridingSpot: RidingSpot): GeofencingRequest {
    val geofence = Geofence.Builder().setRequestId(ridingSpot.id)
        .setCircularRegion(
            ridingSpot.latitude,
            ridingSpot.longitude,
            GEOFENCE_RADIUS_METERS
        ).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .setExpirationDuration(GEOFENCE_DURATION_MILLIS)
        .build()

    return GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
}