package com.arbonik.santehnikaonlinetest.ui.start

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import com.arbonik.santehnikaonlinetest.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager by lazy {
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val _tapRequestState: MutableStateFlow<Resource<Location>?> = MutableStateFlow(null)
    val tapRequestState: StateFlow<Resource<Location>?> = _tapRequestState

    private val listener = LocationListener {
        _tapRequestState.value = Resource.Success(it)
    }

    @SuppressLint("MissingPermission")
    fun startSearch() {
        _tapRequestState.value = Resource.Loading()

        val lastPositionNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val lastPositionGPS =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (lastPositionNetwork == null && lastPositionGPS == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                listener
            )
        } else {
            val point = lastPositionNetwork ?: lastPositionGPS
            _tapRequestState.value = point?.let {
                Resource.Success(it)
            } ?: Resource.Error("No data")
        }
    }

    fun clearSearch() {
        _tapRequestState.value = null
        locationManager.removeUpdates(listener)
    }
}