package com.bikes.greyp.udacitycapstoneproject.ui.base

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.OnMapReadyCallback

abstract class BaseMapFragment : Fragment(), OnMapReadyCallback {

    private val neededPermissions: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    abstract val requestPermissionLauncher: ActivityResultLauncher<Array<String>>


    fun createRequestPermissionLauncher(
        permissionsGrantedAction: () -> Unit,
        permissionsNotGrantedAction: () -> Unit
    ) =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var areAllPermissionsGranted = true
            permissions.entries.forEach { entry ->
                val isGranted = entry.value
                if (!isGranted) {
                    areAllPermissionsGranted = false
                }
            }
            if (areAllPermissionsGranted) {
                permissionsGrantedAction.invoke()
            } else {
                permissionsNotGrantedAction.invoke()
            }
        }


    fun checkAndRequestPermissions() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(neededPermissions)
        }
    }

    fun allPermissionsGranted(): Boolean {
        return neededPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}