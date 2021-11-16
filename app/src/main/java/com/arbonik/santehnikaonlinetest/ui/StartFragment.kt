package com.arbonik.santehnikaonlinetest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arbonik.santehnikaonlinetest.R
import com.arbonik.santehnikaonlinetest.databinding.FragmentStartBinding
import com.google.android.material.snackbar.Snackbar


class StartFragment : Fragment() {

    private val PERMISSION_CODE = 200
    private val locationManager by lazy {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private lateinit var binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toMapButton.setOnClickListener {
            checkGPSPermissions()
        }
    }

    private fun checkGPSPermissions() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_CODE
                )
            } else {
                findPosition()
            } else {
            Snackbar.make(requireView(), getString(R.string.gps_off), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            checkGPSPermissions()
        } else {
            Snackbar.make(requireView(), getString(R.string.no_geo_position), Snackbar.LENGTH_LONG)
                .show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val listener = LocationListener {
        navigateToMapFragment(it)
    }

    private fun navigateToMapFragment(it: Location) {
        findNavController().navigate(
            StartFragmentDirections.actionStartFragmentToMainFragment(
                it.latitude.toFloat(),
                it.longitude.toFloat(),
            )
        )
        binding.toMapButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun findPosition() {
        val lastPositionNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val lastPositionGPS =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (lastPositionNetwork == null && lastPositionGPS == null) {
            binding.toMapButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                listener
            )
        } else {
            val point = lastPositionNetwork ?: lastPositionGPS
            point?.let {
                navigateToMapFragment(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(listener)
    }
}