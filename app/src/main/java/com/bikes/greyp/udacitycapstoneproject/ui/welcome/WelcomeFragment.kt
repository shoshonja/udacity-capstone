package com.bikes.greyp.udacitycapstoneproject.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.fragmentWelcomeButtonRss.setOnClickListener(this)
        binding.fragmentWelcomeButtonMaps.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fragment_welcome_button_rss ->
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToNewsSourceFragment())
            R.id.fragment_welcome_button_maps ->
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToParkMapFragment())
        }
    }
}