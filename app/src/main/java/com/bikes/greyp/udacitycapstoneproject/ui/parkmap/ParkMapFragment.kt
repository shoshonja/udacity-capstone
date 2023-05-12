package com.bikes.greyp.udacitycapstoneproject.ui.parkmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikes.greyp.udacitycapstoneproject.BuildConfig
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentParkMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParkMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    lateinit var binding: FragmentParkMapBinding
    lateinit var adapter: ParkMapAdapter

    private val viewModel: ParkMapViewModel by viewModel()

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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        setObservers()

        if (allPermissionsGranted()) {
            enableMyLocation()
        } else {
            checkAndRequestPermissions()
        }

        viewModel.getRidingSpots()
    }

    private fun setObservers() {
        viewModel.ridingSpots.observe(viewLifecycleOwner, Observer { ridingSpotList ->
            adapter = ParkMapAdapter(ridingSpotList, createItemClickListener(ridingSpotList))
            binding.fragmentParkRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.fragmentParkRecycler.adapter = adapter

            for (ridingSpot in ridingSpotList) {
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            ridingSpot.latitude,
                            ridingSpot.longitude
                        )
                    ).title(ridingSpot.title)
                )
            }
        })
    }

    private fun createItemClickListener(ridingSpotList: List<RidingSpot>): ParkMapAdapter.ClickListener =
        object : ParkMapAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            ridingSpotList[position].latitude,
                            ridingSpotList[position].longitude
                        )
                    )
                )
            }
        }

    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        googleMap.isMyLocationEnabled = true
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
}