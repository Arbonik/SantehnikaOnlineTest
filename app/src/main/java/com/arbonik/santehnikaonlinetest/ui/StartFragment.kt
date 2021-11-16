package com.arbonik.santehnikaonlinetest.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arbonik.santehnikaonlinetest.R
import com.arbonik.santehnikaonlinetest.databinding.FragmentStartBinding


class StartFragment : Fragment(R.layout.fragment_start) {

    private val COARSE_PERMISSION_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStartBinding.inflate(inflater).root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.toMapButton).setOnClickListener {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    COARSE_PERMISSION_CODE
                )
            } else {
                toMapFragment()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == COARSE_PERMISSION_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                toMapFragment()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun toMapFragment() {
        val locationManager = requireContext().getSystemService(LocationManager::class.java)
        val lastPosition =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastPosition?.let {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToMainFragment(
                    it.latitude.toFloat(),
                    it.longitude.toFloat(),
                )
            )
        }
    }
}