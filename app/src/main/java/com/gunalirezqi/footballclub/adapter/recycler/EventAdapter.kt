package com.gunalirezqi.footballclub.adapter.recycler

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.DateTime
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class EventAdapter(private val items: List<Event>, private val listener: (Event) -> Unit) :
    RecyclerView.Adapter<EventViewHolder>() {

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

}

class EventUI : AnkoComponent<ViewGroup> {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(width = matchParent)
                orientation = LinearLayout.VERTICAL

                // padding
                topPadding = dip(16)
                leftPadding = dip(16)
                rightPadding = dip(16)

                cardView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        textView {
                            id = R.id.event_date
                            textSize = 12f //sp
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textColor = R.color.colorPrimaryDark
                        }.lparams(width = matchParent) {
                            topMargin = dip(30)
                            bottomMargin = dip(-35)
                        }
                        textView {
                            id = R.id.event_time
                            textSize = 12f //sp
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textColor = R.color.colorPrimaryDark
                        }.lparams(width = matchParent) {
                            topMargin = dip(35)
                            bottomMargin = dip(-35)
                        }
                        linearLayout {
                            gravity = Gravity.CENTER
                            orientation = LinearLayout.HORIZONTAL
                            linearLayout {
                                gravity = Gravity.CENTER
                                orientation = LinearLayout.VERTICAL
                                imageView {
                                    id = R.id.team_home_badge
                                }.lparams(width = dip(50), height = dip(50))
                                textView {
                                    id = R.id.team_home_name
                                    textSize = 12f //sp
                                }.lparams {
                                    topMargin = dip(5)
                                }
                            }.lparams(width = dip(125), height = dip(100)) {
                                margin = dip(16)
                            }
                            linearLayout {
                                gravity = Gravity.CENTER
                                orientation = LinearLayout.HORIZONTAL
                                textView {
                                    id = R.id.team_home_score
                                    textSize = 25f //sp
                                    setTypeface(typeface, Typeface.BOLD)
                                }
                                textView {
                                    text = ":"
                                    textSize = 14f //sp
                                }.lparams {
                                    leftMargin = dip(8)
                                    rightMargin = dip(8)
                                }
                                textView {
                                    id = R.id.team_away_score
                                    textSize = 25f //sp
                                    setTypeface(typeface, Typeface.BOLD)
                                }
                            }.lparams {
                                margin = dip(16)
                            }
                            linearLayout {
                                gravity = Gravity.CENTER
                                orientation = LinearLayout.VERTICAL
                                imageView {
                                    id = R.id.team_away_badge
                                }.lparams(width = dip(50), height = dip(50))
                                textView {
                                    id = R.id.team_away_name
                                    textSize = 12f //sp
                                }.lparams {
                                    topMargin = dip(5)
                                }
                            }.lparams(width = dip(125), height = dip(100)) {
                                margin = dip(16)
                            }
                        }.lparams(width = matchParent, height = dip(125))
                    }.lparams(width = matchParent)
                }.lparams(width = matchParent)
            }
        }
    }

}

class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val eventDate: TextView = view.find(R.id.event_date)
    private val eventTime: TextView = view.find(R.id.event_time)
    private val teamHomeBadge: ImageView = view.find(R.id.team_home_badge)
    private val teamHomeName: TextView = view.find(R.id.team_home_name)
    private val teamHomeScore: TextView = view.find(R.id.team_home_score)
    private val teamAwayBadge: ImageView = view.find(R.id.team_away_badge)
    private val teamAwayName: TextView = view.find(R.id.team_away_name)
    private val teamAwayScore: TextView = view.find(R.id.team_away_score)

    fun bindItem(
        items: Event,
        listener: (Event) -> Unit
    ) {
        val request = ApiRepository()
        val gson = Gson()

        // Get & Set Home & Away Team Badge
        GlobalScope.launch(Dispatchers.Main) {
            val teamHome = gson.fromJson(
                request
                    .doRequest(TheSportDBApi.getDetailTeam(items.teamHomeId)).await(),
                TeamResponse::class.java
            )

            val teamAway = gson.fromJson(
                request
                    .doRequest(TheSportDBApi.getDetailTeam(items.teamAwayId)).await(),
                TeamResponse::class.java
            )

            Picasso.get().load(teamHome.teams.first().teamBadge).into(teamHomeBadge)
            Picasso.get().load(teamAway.teams.first().teamBadge).into(teamAwayBadge)
        }

        // Event Date & Time
        eventDate.text = items.eventDate?.let { DateTime.getDate(it) }
        eventTime.text = items.eventTime?.let { DateTime.getTime(it) }

        // Home & Away Team Name
        teamHomeName.text = items.teamHomeName
        teamAwayName.text = items.teamAwayName

        // Home & Away Team Score
        teamHomeScore.text = (items.teamHomeScore ?: "-")
        teamAwayScore.text = (items.teamAwayScore ?: "-")

        itemView.setOnClickListener {
            listener(items)
        }
    }
}