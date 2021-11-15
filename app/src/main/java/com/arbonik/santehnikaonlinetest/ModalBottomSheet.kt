package com.arbonik.santehnikaonlinetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arbonik.santehnikaonlinetest.databinding.ModalBottomSheetContentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(
    val data: ShortData
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ModalBottomSheetContentBinding.inflate(inflater)
        binding.address.text = data.address
        binding.lat.text = data.lat.toString()
        binding.lon.text = data.lon.toString()
        binding.button.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMainFragmentToPlaceFragment(
                data
            ))
            dismiss()
        }
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}