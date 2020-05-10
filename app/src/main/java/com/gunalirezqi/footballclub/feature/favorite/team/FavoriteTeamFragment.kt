package com.gunalirezqi.footballclub.feature.favorite.team

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
import com.gunalirezqi.footballclub.adapter.recycler.FavoriteTeamAdapter
import com.gunalirezqi.footballclub.database.FavoriteTeam
import com.gunalirezqi.footballclub.database.database
import com.gunalirezqi.footballclub.feature.detail.team.main.DetailTeamActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteTeamFragment : Fragment() {

    private var favorites: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var adapter: FavoriteTeamAdapter
    private lateinit var listEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var handleMessage: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteTeamAdapter(favorites) {
            startActivity<DetailTeamActivity>("teamId" to it.teamId, "teamName" to it.teamName)
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
        favorites.clear()
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }

        handleMessage.text = if (favorites.isEmpty()) "No favorite team" else ""
    }
}
