package com.gunalirezqi.footballclub.adapter.recycler

import android.graphics.Typeface
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
import com.gunalirezqi.footballclub.model.Standing
import com.gunalirezqi.footballclub.model.TeamResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*

class StandingAdapter(private val items: List<Standing>) : RecyclerView.Adapter<StandingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingViewHolder {
        return StandingViewHolder(
            StandingUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

}

class StandingUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                // padding
                topPadding = dip(16)
                leftPadding = dip(16)
                rightPadding = dip(16)

                imageView {
                    id = R.id.standing_teambadge
                }.lparams(width = dip(35), height = dip(35)) {
                    rightMargin = dip(16)
                }
                textView {
                    id = R.id.standing_name
                    gravity = Gravity.START
                    topPadding = dip(10)
                    textSize = 11f
                }.lparams(width = dip(130), height = matchParent)
                textView {
                    id = R.id.standing_played
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_win
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_draw
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_loss
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_goalsfor
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_goalsagainst
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_goalsdifference
                    gravity = Gravity.CENTER
                    textSize = 11f
                }.lparams(width = dip(25), height = matchParent)
                textView {
                    id = R.id.standing_total
                    gravity = Gravity.CENTER
                    textSize = 11f
                    setTypeface(typeface, Typeface.BOLD)
                }.lparams(width = dip(25), height = matchParent)
            }
        }
    }

}

class StandingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val standingName: TextView = view.find(R.id.standing_name)
    private val standingTeamBadge: ImageView = view.find(R.id.standing_teambadge)
    private val standingPlayed: TextView = view.find(R.id.standing_played)
    private val standingWin: TextView = view.find(R.id.standing_win)
    private val standingDraw: TextView = view.find(R.id.standing_draw)
    private val standingLoss: TextView = view.find(R.id.standing_loss)
    private val standingGoalsFor: TextView = view.find(R.id.standing_goalsfor)
    private val standingGoalsAgainst: TextView = view.find(R.id.standing_goalsagainst)
    private val standingGoalsDifference: TextView = view.find(R.id.standing_goalsdifference)
    private val standingTotal: TextView = view.find(R.id.standing_total)

    fun bindItem(
        items: Standing
    ) {
        val request = ApiRepository()
        val gson = Gson()

        // Get Team Badge
        GlobalScope.launch(Dispatchers.Main) {
            val team = gson.fromJson(
                request
                    .doRequest(TheSportDBApi.getDetailTeam(items.standingTeamId)).await(),
                TeamResponse::class.java
            )

            Picasso.get().load(team.teams.first().teamBadge).into(standingTeamBadge)
        }

        standingName.text = items.standingName
        standingPlayed.text = items.standingPlayed
        standingWin.text = items.standingWin
        standingDraw.text = items.standingDraw
        standingLoss.text = items.standingLoss
        standingGoalsFor.text = items.standingGoalsFor
        standingGoalsAgainst.text = items.standingGoalsAgainst
        standingGoalsDifference.text = items.standingGoalsDifference
        standingTotal.text = items.standingTotal
    }
}