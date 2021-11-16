package com.arbonik.santehnikaonlinetest.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.arbonik.santehnikaonlinetest.data.GeoData
import com.arbonik.santehnikaonlinetest.databinding.ModalBottomSheetContentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ModalBottomSheet"
        private const val TAG_DATA_ARG = "data"
        fun newInstance(data: GeoData): ModalBottomSheet {
            val args = bundleOf(
                TAG_DATA_ARG to data
            )
            val fragment = ModalBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ModalBottomSheetContentBinding.inflate(inflater)
        val data = arguments?.getParcelable<GeoData>(TAG_DATA_ARG) ?: GeoData()
        binding.address.text = data.address
        binding.lat.text = data.lat.toString()
        binding.lon.text = data.lon.toString()
        binding.button.setOnClickListener {
            findNavController().navigate(
                MapFragmentDirections.actionMainFragmentToPlaceFragment(
                    data
                )
            )
            dismiss()
        }
        return binding.root
    }
}