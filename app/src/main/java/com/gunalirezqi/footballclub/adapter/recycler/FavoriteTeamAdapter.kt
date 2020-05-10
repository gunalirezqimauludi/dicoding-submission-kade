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
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.database.FavoriteTeam
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class FavoriteTeamAdapter(private val favorite: List<FavoriteTeam>, private val listener: (FavoriteTeam) -> Unit) :
    RecyclerView.Adapter<FavoriteTeamViewHolder>() {

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamViewHolder {
        return FavoriteTeamViewHolder(
            FavoriteTeamUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        holder.bindItem(favorite[position], listener)
    }

    override fun getItemCount(): Int = favorite.size

}

class FavoriteTeamUI : AnkoComponent<ViewGroup> {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                }.lparams(width = dip(50), height = dip(50)) {
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

class FavoriteTeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val teamBadge: ImageView = view.find(R.id.team_badge)
    private val teamName: TextView = view.find(R.id.team_name)
    private val teamDescription: TextView = view.find(R.id.team_description)

    fun bindItem(favorite: FavoriteTeam, listener: (FavoriteTeam) -> Unit) {

        Picasso.get().load(favorite.teamBadge).into(teamBadge)
        teamName.text = favorite.teamName
        teamDescription.text = favorite.teamDescription?.substring(0, 125)

        itemView.setOnClickListener { listener(favorite) }
    }
}