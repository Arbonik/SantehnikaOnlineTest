package com.arbonik.santehnikaonlinetest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.arbonik.santehnikaonlinetest.databinding.FragmentPlaceBinding

class PlaceFragment : Fragment() {

    private lateinit var binding: FragmentPlaceBinding
    private val args: PlaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addressTextField.setText(args.data.address)
        binding.lanTextField.setText(args.data.lat.toString())
        binding.lonTextField.setText(args.data.lon.toString())
    }
}