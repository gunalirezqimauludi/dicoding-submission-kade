package com.gunalirezqi.footballclub.feature.standing

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.recycler.StandingAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.model.Standing
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class StandingFragment : Fragment(), StandingView {

    private lateinit var presenter: StandingPresenter
    private lateinit var adapter: StandingAdapter

    private lateinit var listStanding: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var leagueId: String

    private var standing: MutableList<Standing> = mutableListOf()

    companion object {
        private const val ARG_LEAGUE_ID = ""

        fun newInstance(leagueId: String): StandingFragment {
            val args = Bundle()
            args.putString(ARG_LEAGUE_ID, leagueId)
            val fragment = StandingFragment()
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

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL

                            // padding
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            textView {
                                text = context.getString(R.string.label_team)
                                gravity = Gravity.START
                                topPadding = dip(10)
                                textSize = 11f
                            }.lparams(width = dip(35), height = dip(35)) {
                                rightMargin = dip(16)
                            }
                            textView {
                                gravity = Gravity.START
                                textSize = 11f
                            }.lparams(width = dip(130), height = matchParent)
                            textView {
                                text = "P"
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = "W"
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = "D"
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = "L"
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = context.getString(R.string.label_goalsfor)
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = context.getString(R.string.label_goalsagainst)
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = context.getString(R.string.label_goalsdifference)
                                gravity = Gravity.CENTER
                                textSize = 11f
                            }.lparams(width = dip(25), height = matchParent)
                            textView {
                                text = context.getString(R.string.label_points)
                                gravity = Gravity.CENTER
                                textSize = 11f
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(25), height = matchParent)
                        }

                        listStanding = recyclerView {
                            lparams(width = matchParent, height = wrapContent) {
                                bottomMargin = dip(16)
                            }
                            layoutManager = LinearLayoutManager(context)
                        }
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerInParent()
                    }
                }
            }

            // Adapter
            adapter = StandingAdapter(standing)
            listStanding.adapter = adapter

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            presenter = StandingPresenter(this@StandingFragment, request, gson)
            presenter.getStandings(leagueId)

            swipeRefresh.onRefresh {
                presenter.getStandings(leagueId)
            }
        }.view
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showStandings(data: List<Standing>) {
        swipeRefresh.isRefreshing = false
        standing.clear()
        standing.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
