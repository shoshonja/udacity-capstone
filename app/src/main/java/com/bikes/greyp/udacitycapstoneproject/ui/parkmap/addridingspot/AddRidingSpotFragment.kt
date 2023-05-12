package com.bikes.greyp.udacitycapstoneproject.ui.parkmap.addridingspot

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.bikes.greyp.udacitycapstoneproject.BuildConfig
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentAddRidingSpotBinding
import com.bikes.greyp.udacitycapstoneproject.ui.base.BaseMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.snackbar.Snackbar

class AddRidingSpotFragment : BaseMapFragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    lateinit var binding: FragmentAddRidingSpotBinding

    override val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        createRequestPermissionLauncher({ enableMyLocation() }, { createSnackbar() })

    //TODO save instance state here

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddRidingSpotBinding.inflate(inflater, container, false)

        mapView = binding.fragmentAddRidingSpotMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        handlePermissions()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun handlePermissions(){
        if (allPermissionsGranted()) {
            enableMyLocation()
        } else {
            checkAndRequestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        googleMap.isMyLocationEnabled = true
    }

    private fun createSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.fragment_park_map_info_permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.settings) {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }.show()
    }
}