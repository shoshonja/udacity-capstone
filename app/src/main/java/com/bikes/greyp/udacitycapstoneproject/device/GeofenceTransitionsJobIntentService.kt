package com.bikes.greyp.udacitycapstoneproject.device

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.data.repository.RidingSpotRepository
import com.bikes.greyp.udacitycapstoneproject.utils.sendNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                geofencingEvent.triggeringGeofences?.let { sendNotification(it) }
            }
        }
    }

    private fun sendNotification(triggeringGeofences: List<Geofence>) {
        val ridingSpotRepository: RidingSpotRepository by inject()

        for (geofence in triggeringGeofences) {
            val requestId = geofence.requestId
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                //get the reminder with the request id
                val result = ridingSpotRepository.getRidingSpot(requestId)
                if (result is RidingSpot) {
                    //send a notification to the user with the reminder details
                    sendNotification(
                        this@GeofenceTransitionsJobIntentService, RidingSpot(
                            title = result.title,
                            latitude = result.latitude,
                            longitude = result.longitude,
                            id = result.id
                        )
                    )
                }
            }
        }
    }
}