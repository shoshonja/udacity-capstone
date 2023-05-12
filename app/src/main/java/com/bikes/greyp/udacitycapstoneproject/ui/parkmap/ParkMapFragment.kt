package com.bikes.greyp.udacitycapstoneproject.ui.parkmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bikes.greyp.udacitycapstoneproject.BuildConfig
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentParkMapBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParkMapFragment : Fragment(), OnMapReadyCallback {

    private val neededPermissions: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var areAllPermissionsGranted = true
            permissions.entries.forEach { entry ->
                val isGranted = entry.value
                if (!isGranted) {
                    areAllPermissionsGranted = false
                }
            }
            if (areAllPermissionsGranted) {
                enableMyLocation()
            } else {
                createSnackbar()
            }
        }


    private val viewModel: ParkMapViewModel by viewModel()

    private lateinit var binding: FragmentParkMapBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkMapBinding.inflate(inflater, container, false)

        mapView = binding.fragmentParkMapMapview
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        Toast.makeText(requireContext(), "Map is ready!", Toast.LENGTH_SHORT).show()
        setObservers()

        if (allPermissionsGranted()) {
            enableMyLocation()
        } else {
            checkAndRequestPermissions()
        }
        viewModel.getRidingSpots()
    }

    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        googleMap.isMyLocationEnabled = true
        zoomToMyLocation(googleMap)
    }

    @SuppressLint("MissingPermission")
    private fun zoomToMyLocation(googleMap: GoogleMap) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                latitude,
                                longitude
                            ), 15f
                        )
                    )
                }
            }
    }

    private fun setObservers() {
        viewModel.ridingSpots.observe(viewLifecycleOwner, Observer { ridingSpotList ->
            for (ridingSpot in ridingSpotList) {
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            ridingSpot.latitude,
                            ridingSpot.longitude
                        )
                    ).title(ridingSpot.title)
                )
                if (allPermissionsGranted()) {
//                    createGeofence(requireActivity(), ridingSpot)
                }
            }
        })
    }

    private fun checkAndRequestPermissions() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(neededPermissions)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return neededPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
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

    //request permissions!
    //load all pins - DONE
    //show all pins - DONE
    //enable geofencing notification for pins - DONE
    //create FAB button for creating a new pin
    //pin click shows park information - DONE
    //app zooms to your location
    //create snackbar with info. Click on it to dismiss. Show on each launch
}