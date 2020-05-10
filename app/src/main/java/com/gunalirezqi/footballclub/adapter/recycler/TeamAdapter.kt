package com.gunalirezqi.footballclub.adapter.recycler

import android.annotation.SuppressLint
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
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.model.TeamResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*

class TeamAdapter(private val items: List<Team>, private val listener: (Team) -> Unit)
    : RecyclerView.Adapter<TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            TeamUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

}

class TeamUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                // padding
                topPadding = dip(16)
                leftPadding = dip(16)
                rightPadding = dip(16)

                imageView {
                    id = R.id.team_badge
                }.lparams(width = dip(50), height = dip(50)){
                    rightMargin = dip(16)
                }

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    textView {
                        id = R.id.team_name
                        gravity = Gravity.START
                        textSize = 14f
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        id = R.id.team_description
                        textSize = 11f
                    }
                }
            }
        }
    }

}

class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val teamName: TextView = view.find(R.id.team_name)
    private val teamBadge: ImageView = view.find(R.id.team_badge)
    private val teamDescription: TextView = view.find(R.id.team_description)

    @SuppressLint("SetTextI18n")
    fun bindItem(
        items: Team,
        listener: (Team) -> Unit
    ) {
        val request = ApiRepository()
        val gson = Gson()

        // Get Team Badge
        GlobalScope.launch(Dispatchers.Main) {
            val team = gson.fromJson(request
                .doRequest(TheSportDBApi.getDetailTeam(items.teamId)).await(),
                TeamResponse::class.java
            )

            Picasso.get().load(team.teams.first().teamBadge).into(teamBadge)
        }

        teamName.text = items.teamName
        teamDescription.text = items.teamDescription?.substring(0, 125)

        itemView.setOnClickListener {
            listener(items)
        }
    }
}