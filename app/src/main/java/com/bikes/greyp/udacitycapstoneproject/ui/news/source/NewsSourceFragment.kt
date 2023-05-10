package com.bikes.greyp.udacitycapstoneproject.ui.news.source

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bikes.greyp.udacitycapstoneproject.R
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentNewsSourceBinding

class NewsSourceFragment : Fragment(), OnClickListener {

    lateinit var binding: FragmentNewsSourceBinding
    lateinit var viewModel: NewsSourceViewModel
    //TODO inject this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsSourceBinding.inflate(inflater, container, false)
        binding.fragmentNewsSourceButtonVt.setOnClickListener(this)
        binding.fragmentNewsSourceButtonPb.setOnClickListener(this)

        //TODO remove this, constructor is not default, and this should be done via Koin
        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[NewsSourceViewModel::class.java]

        setObservers()

        return binding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fragment_news_source_button_vt -> viewModel.checkConnectionAndGetFeeds(RssSource.Vital)
            R.id.fragment_news_source_button_pb -> viewModel.checkConnectionAndGetFeeds(RssSource.Pinkbike)
        }
    }

    private fun setObservers() = with(viewModel) {
        rssSource.observe(viewLifecycleOwner, Observer { rssSource ->
            findNavController().navigate(
                NewsSourceFragmentDirections.actionNewsSourceFragmentToNewsFeedFragment(
                    rssSource
                )
            )
        })
        internetConnection.observe(viewLifecycleOwner, Observer { connection ->
            if (!connection) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fragment_news_source_error_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}