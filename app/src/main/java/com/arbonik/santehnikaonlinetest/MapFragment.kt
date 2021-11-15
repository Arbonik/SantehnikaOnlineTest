package com.arbonik.santehnikaonlinetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.arbonik.santehnikaonlinetest.databinding.FragmentMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mapView by lazy { binding.mapview }

    private val args: MapFragmentArgs by navArgs()

    val searchManager: SearchManager by lazy {
        SearchFactory.getInstance().createSearchManager(
            SearchManagerType.ONLINE
        )
    }

    val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(p0: Response) {
            val obj = p0.collection.children.firstOrNull()?.obj!!
            val toponim = obj.metadataContainer.getItem(ToponymObjectMetadata::class.java)
            val address = toponim.address
            val point = toponim.balloonPoint
            point.let {
                val d = ShortData(it.latitude, it.longitude, address.formattedAddress)
                val modalBottomSheet = ModalBottomSheet(d)
                modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
            }
        }

        override fun onSearchError(p0: Error) {}
    }

    val mapListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            searchManager.submit(p1, 16, SearchOptions(), searchListener)
        }
        override fun onMapLongTap(p0: Map, p1: Point) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.map.move(
            CameraPosition(
                Point(args.lat.toDouble(), args.lon.toDouble()), 11f, 0f, 0f
            ),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        mapView.map.addInputListener(mapListener)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }
}