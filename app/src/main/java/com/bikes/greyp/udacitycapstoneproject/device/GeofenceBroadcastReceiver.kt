package com.bikes.greyp.udacitycapstoneproject.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bikes.greyp.udacitycapstoneproject.utils.ACTION_GEOFENCE_EVENT

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
        }
    }
}