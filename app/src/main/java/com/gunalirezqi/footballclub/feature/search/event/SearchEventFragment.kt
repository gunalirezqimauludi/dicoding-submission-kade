package com.gunalirezqi.footballclub.feature.search.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.recycler.EventAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.feature.detail.event.DetailEventActivity
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class SearchEventFragment : Fragment(), SearchEventView {

    private lateinit var eventPresenter: SearchEventPresenter
    private lateinit var adapter: EventAdapter

    private lateinit var listEvent: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var message: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var keyword: String

    private var event: MutableList<Event> = mutableListOf()

    companion object {
        private const val ARG_KEYWORD = ""

        fun newInstance(keyword: String): SearchEventFragment {
            val args = Bundle()
            args.putString(ARG_KEYWORD, keyword)
            val fragment = SearchEventFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            keyword = arguments?.getString(ARG_KEYWORD) ?: "ARG_KEYWORD"
        }

        return UI {

            // UI Layout
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    listEvent = recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(context)
                    }

                    message = textView {
                    }.lparams {
                        centerInParent()
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerInParent()
                    }
                }
            }

            // Adapter
            adapter = EventAdapter(event) {
                startActivity<DetailEventActivity>("eventId" to it.eventId, "eventName" to it.eventName)
            }
            listEvent.adapter = adapter

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            eventPresenter = SearchEventPresenter(this@SearchEventFragment, request, gson)
            eventPresenter.getEventSearch(keyword)

            swipeRefresh.onRefresh {
                eventPresenter.getEventSearch(keyword)
            }
        }.view
    }

    @SuppressLint("SetTextI18n")
    fun handleNotFound() {
        message.text = "No result found"
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showEventSearch(data: List<Event>) {
        swipeRefresh.isRefreshing = false
        event.clear()
        event.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
