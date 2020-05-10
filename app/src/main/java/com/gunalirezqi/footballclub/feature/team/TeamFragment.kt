package com.gunalirezqi.footballclub.feature.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.recycler.TeamAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.feature.detail.team.main.DetailTeamActivity
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamFragment : Fragment(), TeamView {

    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamAdapter

    private lateinit var listTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var leagueId: String

    private var team: MutableList<Team> = mutableListOf()

    companion object {
        private const val ARG_LEAGUE_ID = ""

        fun newInstance(leagueId: String): TeamFragment {
            val args = Bundle()
            args.putString(ARG_LEAGUE_ID, leagueId)
            val fragment = TeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            leagueId = arguments?.getString(ARG_LEAGUE_ID) ?: "ARG_LEAGUE_ID"
        }

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

                    listTeam = recyclerView {
                        lparams(width = matchParent, height = wrapContent) {
                            bottomMargin = dip(16)
                        }
                        layoutManager = LinearLayoutManager(context)
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
            presenter = TeamPresenter(this@TeamFragment, request, gson)
            presenter.getTeams(leagueId)

            swipeRefresh.onRefresh {
                presenter.getTeams(leagueId)
            }
        }.view
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeams(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        team.clear()
        team.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
