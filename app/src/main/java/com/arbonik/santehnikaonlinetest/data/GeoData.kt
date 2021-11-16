package com.arbonik.santehnikaonlinetest.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoData(
    val lat : Double = 0.0,
    val lon : Double = 0.0,
    val address : String = "",
) : Parcelable
