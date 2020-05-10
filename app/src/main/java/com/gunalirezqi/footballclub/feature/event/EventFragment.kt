package com.gunalirezqi.footballclub.feature.event

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

class EventFragment : Fragment(), EventView {

    private lateinit var presenter: EventPresenter
    private lateinit var adapterNext: EventAdapter
    private lateinit var adapterPrevious: EventAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var listEventNext: RecyclerView
    private lateinit var progressBarEventNext: ProgressBar
    private lateinit var messageNotFoundEventNext: TextView
    private lateinit var listEventPrevious: RecyclerView
    private lateinit var progressBarEventPrevious: ProgressBar
    private lateinit var messageNotFoundEventPrevious: TextView

    private lateinit var leagueId: String

    private var eventNext: MutableList<Event> = mutableListOf()
    private var eventPrevious: MutableList<Event> = mutableListOf()

    companion object {
        private const val ARG_LEAGUE_ID = ""

        fun newInstance(leagueId: String): EventFragment {
            val args = Bundle()
            args.putString(ARG_LEAGUE_ID, leagueId)
            val fragment = EventFragment()
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

                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        relativeLayout {
                            lparams(width = matchParent, height = wrapContent)
                            linearLayout {
                                orientation = LinearLayout.VERTICAL

                                textView {
                                    topPadding = dip(16)
                                    leftPadding = dip(16)

                                    text = context.getString(R.string.label_next_match)
                                    textSize = 18f
                                    setTypeface(typeface, Typeface.BOLD)
                                }

                                listEventNext = recyclerView {
                                    lparams(width = matchParent, height = wrapContent)
                                    layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                                }
                            }

                            messageNotFoundEventNext = textView {
                            }.lparams {
                                topMargin = dip(325)
                                centerInParent()
                            }

                            progressBarEventNext = progressBar {
                            }.lparams {
                                topMargin = dip(325)
                                centerInParent()
                            }
                        }
                    }.lparams(width = matchParent, height = dip(210))

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        relativeLayout {
                            lparams(width = matchParent, height = wrapContent)

                            linearLayout {
                                orientation = LinearLayout.VERTICAL

                                textView {
                                    topPadding = dip(16)
                                    leftPadding = dip(16)

                                    text = context.getString(R.string.label_last_match)
                                    textSize = 18f
                                    setTypeface(typeface, Typeface.BOLD)
                                }

                                listEventPrevious = recyclerView {
                                    lparams(width = matchParent, height = wrapContent)
                                    layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                                }
                            }

                            messageNotFoundEventPrevious = textView {
                            }.lparams {
                                topMargin = dip(325)
                                centerInParent()
                            }

                            progressBarEventPrevious = progressBar {
                            }.lparams {
                                topMargin = dip(325)
                                centerInParent()
                            }
                        }
                    }.lparams(width = matchParent, height = dip(210))
                }
            }

            // Adapter Event Next
            adapterNext = EventAdapter(eventNext) {
                startActivity<DetailEventActivity>("eventId" to it.eventId, "eventName" to it.eventName)
            }
            listEventNext.adapter = adapterNext

            // Adapter Event Previous
            adapterPrevious = EventAdapter(eventPrevious) {
                startActivity<DetailEventActivity>("eventId" to it.eventId, "eventName" to it.eventName)
            }
            listEventPrevious.adapter = adapterPrevious

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            presenter = EventPresenter(this@EventFragment, request, gson)

            // Get Event Next & Previous
            presenter.getEventNext(leagueId)
            presenter.getEventPrevious(leagueId)

            swipeRefresh.onRefresh {
                presenter.getEventNext(leagueId)
                presenter.getEventPrevious(leagueId)
            }
        }.view
    }

    @SuppressLint("SetTextI18n")
    fun showNotFoundEventNext() {
        messageNotFoundEventNext.text = "No match next"
    }

    @SuppressLint("SetTextI18n")
    fun showNotFoundEventPrevious() {
        messageNotFoundEventPrevious.text = "No match previous"
    }

    override fun showLoadingEventNext() {
        progressBarEventNext.visible()
    }

    override fun showLoadingEventPrevious() {
        progressBarEventPrevious.visible()
    }

    override fun hideLoadingEventNext() {
        progressBarEventNext.invisible()
    }

    override fun hideLoadingEventPrevious() {
        progressBarEventPrevious.invisible()
    }

    override fun showEventNext(data: List<Event>) {
        swipeRefresh.isRefreshing = false
        eventNext.clear()
        eventNext.addAll(data)
        adapterNext.notifyDataSetChanged()
    }

    override fun showEventPrevious(data: List<Event>) {
        swipeRefresh.isRefreshing = false
        eventPrevious.clear()
        eventPrevious.addAll(data)
        adapterPrevious.notifyDataSetChanged()
    }
}
