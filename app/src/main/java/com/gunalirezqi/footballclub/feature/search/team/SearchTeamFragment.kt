package com.gunalirezqi.footballclub.feature.search.team

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
import com.gunalirezqi.footballclub.adapter.recycler.TeamAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.feature.detail.team.main.DetailTeamActivity
import com.gunalirezqi.footballclub.feature.search.event.SearchTeamView
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class SearchTeamFragment : Fragment(), SearchTeamView {

    private lateinit var teamPresenter: SearchTeamPresenter
    private lateinit var adapter: TeamAdapter

    private lateinit var listTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var message: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var keyword: String

    private var team: MutableList<Team> = mutableListOf()

    companion object {
        private const val ARG_KEYWORD = ""

        fun newInstance(keyword: String): SearchTeamFragment {
            val args = Bundle()
            args.putString(ARG_KEYWORD, keyword)
            val fragment = SearchTeamFragment()
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
                    listTeam = recyclerView {
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
            adapter = TeamAdapter(team) {
                startActivity<DetailTeamActivity>("teamId" to it.teamId, "teamName" to it.teamName)
            }
            listTeam.adapter = adapter

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            teamPresenter = SearchTeamPresenter(this@SearchTeamFragment, request, gson)
            teamPresenter.getTeamSearch(keyword)

            swipeRefresh.onRefresh {
                teamPresenter.getTeamSearch(keyword)
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

    override fun showTeamSearch(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        team.clear()
        team.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
