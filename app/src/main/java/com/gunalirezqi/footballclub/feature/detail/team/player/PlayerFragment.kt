package com.gunalirezqi.footballclub.feature.detail.team.player

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
import com.gunalirezqi.footballclub.adapter.recycler.PlayerAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.feature.detail.player.DetailPlayerActivity
import com.gunalirezqi.footballclub.model.Player
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PlayerFragment : Fragment(), PlayerView {

    private lateinit var presenter: PlayerPresenter
    private lateinit var adapter: PlayerAdapter

    private lateinit var listPlayer: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var message: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var teamId: String

    private var player: MutableList<Player> = mutableListOf()

    companion object {
        private const val ARG_LEAGUE_ID = ""

        fun newInstance(teamId: String): PlayerFragment {
            val args = Bundle()
            args.putString(ARG_LEAGUE_ID, teamId)
            val fragment = PlayerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            teamId = arguments?.getString(ARG_LEAGUE_ID) ?: "ARG_LEAGUE_ID"
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

                    listPlayer = recyclerView {
                        lparams(width = matchParent, height = wrapContent) {
                            bottomMargin = dip(16)
                        }
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
            adapter = PlayerAdapter(player) {
                startActivity<DetailPlayerActivity>("playerId" to it.playerId, "playerName" to it.playerName)
            }
            listPlayer.adapter = adapter

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            presenter = PlayerPresenter(this@PlayerFragment, request, gson)
            presenter.getPlayer(teamId)

            swipeRefresh.onRefresh {
                presenter.getPlayer(teamId)
            }
        }.view
    }

    @SuppressLint("SetTextI18n")
    fun handleNotFound() {
        message.text = "No player"
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPlayer(data: List<Player>) {
        swipeRefresh.isRefreshing = false
        player.clear()
        player.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
