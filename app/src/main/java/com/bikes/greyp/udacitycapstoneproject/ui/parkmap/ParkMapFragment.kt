package com.bikes.greyp.udacitycapstoneproject.ui.parkmap

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikes.greyp.udacitycapstoneproject.BuildConfig
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentParkMapBinding
import com.bikes.greyp.udacitycapstoneproject.ui.base.BaseMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ParkMapFragment : BaseMapFragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var adapter: ParkMapAdapter

    lateinit var binding: FragmentParkMapBinding

    private val viewModel: ParkMapViewModel by viewModel()
    override val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        createRequestPermissionLauncher({ enableMyLocation() }, { createSnackbar() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentParkMapBinding.inflate(inflater, container, false)
        binding.fragmentParkMapFab.setOnClickListener {
            findNavController().navigate(ParkMapFragmentDirections.actionParkMapFragmentToAddRidingSpotFragment())
        }

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
        handlePermissions()

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