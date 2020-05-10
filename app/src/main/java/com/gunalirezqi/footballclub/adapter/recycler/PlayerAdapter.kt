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
import com.gunalirezqi.footballclub.model.Player
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class PlayerAdapter(private val items: List<Player>, private val listener: (Player) -> Unit) :
    RecyclerView.Adapter<PlayerViewHolder>() {

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            PlayerUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

}

class PlayerUI : AnkoComponent<ViewGroup> {
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
                    id = R.id.player_photo
                }.lparams(width = dip(50), height = dip(50)) {
                    rightMargin = dip(16)
                }

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    textView {
                        id = R.id.player_name
                        gravity = Gravity.START
                        textSize = 14f
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        id = R.id.player_description
                        textSize = 11f
                    }
                }
            }
        }
    }

}

class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val playerName: TextView = view.find(R.id.player_name)
    private val playerPhoto: ImageView = view.find(R.id.player_photo)
    private val playerDescription: TextView = view.find(R.id.player_description)

    fun bindItem(
        items: Player,
        listener: (Player) -> Unit
    ) {
        playerName.text = items.playerName
        Picasso.get().load(items.playerCutout).into(playerPhoto)
        playerDescription.text = items.playerDescription?.substring(0, 125)

        itemView.setOnClickListener {
            listener(items)
        }
    }
}