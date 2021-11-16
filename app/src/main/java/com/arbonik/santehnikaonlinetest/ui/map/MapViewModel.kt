package com.arbonik.santehnikaonlinetest.ui.map

import androidx.lifecycle.ViewModel
import com.arbonik.santehnikaonlinetest.utils.Resource
import com.arbonik.santehnikaonlinetest.data.GeoData
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {

    private val searchManager: SearchManager by lazy {
        SearchFactory.getInstance().createSearchManager(
            SearchManagerType.ONLINE
        )
    }

    private val _tapRequestState: MutableStateFlow<Resource<GeoData>?> = MutableStateFlow(null)
    val tapRequestState: StateFlow<Resource<GeoData>?> = _tapRequestState

    private val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(p0: Response) {
            val obj = p0.collection.children.firstOrNull()?.obj!!
            val toponim = obj.metadataContainer.getItem(ToponymObjectMetadata::class.java)
            val address = toponim.address
            val point = toponim.balloonPoint
            _tapRequestState.value = Resource.Success(
                GeoData(
                    point.latitude,
                    point.longitude,
                    address.formattedAddress
                )
            )
        }

        override fun onSearchError(p0: Error) {
            _tapRequestState.value = Resource.Error(p0.toString())
        }
    }

    fun startSearch(point: Point){
        _tapRequestState.value = Resource.Loading()
        searchManager.submit(point, 16, SearchOptions(), searchListener)
    }
    fun clearSearch(){
        _tapRequestState.value = null
    }
    val mapListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            startSearch(p1)
        }
        override fun onMapLongTap(p0: Map, p1: Point) {}
    }
}