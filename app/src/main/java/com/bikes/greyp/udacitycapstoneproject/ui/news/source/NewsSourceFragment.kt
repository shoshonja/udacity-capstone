package com.bikes.greyp.udacitycapstoneproject.ui.news.source

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentNewsSourceBinding

class NewsSourceFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentNewsSourceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsSourceBinding.inflate(inflater, container, false)
        binding.fragmentNewsSourceButtonVt.setOnClickListener(this)
        binding.fragmentNewsSourceButtonPb.setOnClickListener(this)

        return binding.root
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fragment_news_source_button_vt -> findNavController().navigate(
                NewsSourceFragmentDirections.actionNewsSourceFragmentToNewsFeedFragment(
                    RssSource.Vital
                )
            )
            R.id.fragment_news_source_button_pb -> findNavController().navigate(
                NewsSourceFragmentDirections.actionNewsSourceFragmentToNewsFeedFragment(
                    RssSource.Pinkbike
                )
            )
        }
    }
}