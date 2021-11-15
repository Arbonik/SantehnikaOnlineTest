package com.arbonik.santehnikaonlinetest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShortData(
    val lat : Double = 0.0,
    val lon : Double = 0.0,
    val address : String = "",
) : Parcelable
