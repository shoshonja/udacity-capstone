package com.bikes.greyp.udacitycapstoneproject.ui.parkmap.addridingspot

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bikes.greyp.udacitycapstoneproject.BuildConfig
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentAddRidingSpotBinding
import com.bikes.greyp.udacitycapstoneproject.ui.base.BaseMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AddRidingSpotFragment : BaseMapFragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentAddRidingSpotBinding

    private val viewModel: AddRidingSpotViewModel by viewModel()
    private var markerHolder: Marker? = null

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

        binding.fragmentAddRidingSpotButtonSave.setOnClickListener {
            handleSaveButtonClick()
        }

        viewModel.ridingSpotSaved.observe(viewLifecycleOwner, Observer { saved ->
            if(saved){
                findNavController().popBackStack()
            }
        })

        return binding.root
    }

    private fun handleSaveButtonClick() {
        if (isRidingSpotDataValid())
            with(binding){
                viewModel.saveRidingSpot(
                    fragmentAddRidingSpotTvTitle.text.toString(),
                    fragmentAddRidingSpotTvLocation.text.toString(),
                    fragmentAddRidingSpotTvDescription.text.toString(),
                    markerHolder!!.position
                )
            }
        else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fragment_add_riding_spot_error_invalid_input),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isRidingSpotDataValid() =
        !(markerHolder == null || binding.fragmentAddRidingSpotTvTitle.text.isEmpty())

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        handlePermissions()
        setMapLongClick(googleMap)
        setPoiClick(googleMap)
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

    private fun handlePermissions() {
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

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            clearExistingMarkers(map)
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            markerHolder = map.addMarker(
                MarkerOptions().position(latLng)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            clearExistingMarkers(map)
            markerHolder = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
            )
            markerHolder!!.showInfoWindow()
        }
    }

    private fun clearExistingMarkers(map: GoogleMap) {
        map.clear()
        markerHolder = null
    }
}