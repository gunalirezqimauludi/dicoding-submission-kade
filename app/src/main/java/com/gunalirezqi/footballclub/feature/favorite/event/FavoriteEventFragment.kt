package com.gunalirezqi.footballclub.feature.favorite.event

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.recycler.FavoriteEventAdapter
import com.gunalirezqi.footballclub.database.FavoriteEvent
import com.gunalirezqi.footballclub.database.database
import com.gunalirezqi.footballclub.feature.detail.event.DetailEventActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteEventFragment : Fragment() {

    private var favoriteEvents: MutableList<FavoriteEvent> = mutableListOf()
    private lateinit var adapter: FavoriteEventAdapter
    private lateinit var listEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var handleMessage: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteEventAdapter(favoriteEvents) {
            startActivity<DetailEventActivity>("eventId" to it.eventId, "eventName" to it.eventName)
        }

        listEvent.adapter = adapter
        swipeRefresh.onRefresh {
            showFavorite()
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return UI {

            // UI Layout
            swipeRefresh = swipeRefreshLayout {
                id = R.id.content
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    listEvent = recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(context)
                    }

                    handleMessage = textView {
                    }.lparams {
                        centerInParent()
                    }
                }
            }
        }.view
    }

    private fun showFavorite() {
        favoriteEvents.clear()
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            favoriteEvents.addAll(favorite)
            adapter.notifyDataSetChanged()
        }

        handleMessage.text = if (favoriteEvents.isEmpty()) "No favorite event" else ""
    }
}
