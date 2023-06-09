package com.bikes.greyp.udacitycapstoneproject.ui.news.newsfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.databinding.FragmentNewsFeedBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsFeedFragment : Fragment() {

    private val args: NewsFeedFragmentArgs by navArgs()
    private val viewModel: NewsFeedViewModel by viewModel()

    lateinit var binding: FragmentNewsFeedBinding
    lateinit var adapter: NewsFeedAdapter
    lateinit var rssSource: RssSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rssSource = args.rssSource
        binding = FragmentNewsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        binding.fragmentNewsFeedProgressBar.visibility = View.VISIBLE
        viewModel.getFeeds(rssSource)
    }

    private fun setObservers() {
        viewModel.retrievedRssFeeds.observe(viewLifecycleOwner, Observer { partialFeedItemList ->
            adapter = NewsFeedAdapter(
                partialFeedItemList,
                rssSource,
                createItemClickListener(partialFeedItemList)
            )
            binding.fragmentNewsFeedRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
            binding.fragmentNewsFeedRecyclerView.adapter = adapter

            binding.fragmentNewsFeedProgressBar.visibility = View.GONE
        })

        viewModel.retrievedRssFeedError.observe(viewLifecycleOwner) { it ->
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            binding.fragmentNewsFeedProgressBar.visibility = View.GONE
        }
    }

    private fun createItemClickListener(partialFeedItemList: List<PartialFeedItem>): NewsFeedAdapter.ClickListener =
        object : NewsFeedAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(partialFeedItemList[position].link))
                startActivity(browserIntent)
            }
        }
}