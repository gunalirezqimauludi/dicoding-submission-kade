package com.gunalirezqi.footballclub.feature.detail.player

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.model.Player
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class DetailPlayerActivity : AppCompatActivity(), DetailPlayerView {

    private lateinit var progressBar: ProgressBar

    private lateinit var presenter: DetailPlayerPresenter

    private lateinit var playerId: String
    private lateinit var playerName: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get Intent player Value
        playerId = intent.getStringExtra("playerId")
        playerName = intent.getStringExtra("playerName")

        // ActionBar Config
        supportActionBar?.title = playerName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // UI Layout
        scrollView {
            relativeLayout {
                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    // Player Header
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        backgroundColor = Color.parseColor("#008577")
                        padding = dip(16)

                        linearLayout {
                            orientation = LinearLayout.VERTICAL

                            imageView {
                                id = R.id.player_photo
                            }.lparams(width = dip(100), height = dip(100)) {
                                gravity = Gravity.CENTER
                            }
                            textView {
                                id = R.id.player_name
                                gravity = Gravity.CENTER
                                topPadding = dip(10)
                                textSize = 25f
                                textColor = Color.WHITE
                                setTypeface(typeface, Typeface.BOLD)
                            }
                            textView {
                                id = R.id.player_position
                                gravity = Gravity.CENTER
                                topPadding = dip(7)
                                textSize = 14f
                                textColor = Color.WHITE
                            }
                        }.lparams(width = matchParent)
                    }.lparams(matchParent, wrapContent)

                    // Player Detail
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        padding = dip(16)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_name)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_detail_name
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_nationality)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_nationality
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_date_born)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_date_born
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_birth_location)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_birth_location
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_position)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_detail_position
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_height)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_height
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_player_weight)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.player_weight
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            padding = dip(2)

                            textView {
                                topPadding = dip(8)
                                text = context.getString(R.string.label_player_description)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }

                            textView {
                                topPadding = dip(2)

                                id = R.id.player_description
                                textSize = 12f //sp
                                gravity = Gravity.FILL_HORIZONTAL
                            }
                        }
                    }
                }

                progressBar = progressBar {
                }.lparams {
                    centerInParent()
                }
            }
        }

        // Presenter
        val request = ApiRepository()
        val gson = Gson()
        presenter = DetailPlayerPresenter(this, request, gson)
        presenter.getPlayerDetail(playerId)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun showPlayerDetail(data: List<Player>) {

        // Get First Data
        val items = data.first()

        // Header
        val playerName: TextView = findViewById(R.id.player_name)
        val playerPhoto: ImageView = findViewById(R.id.player_photo)
        val playerPosition: TextView = findViewById(R.id.player_position)

        // Detail
        val playerDetailName: TextView = findViewById(R.id.player_detail_name)
        val playerDetailPosition: TextView = findViewById(R.id.player_detail_position)
        val playerNationality: TextView = findViewById(R.id.player_nationality)
        val playerDateBorn: TextView = findViewById(R.id.player_date_born)
        val playerBirthLocation: TextView = findViewById(R.id.player_birth_location)
        val playerHeight: TextView = findViewById(R.id.player_height)
        val playerWeight: TextView = findViewById(R.id.player_weight)
        val playerDescription: TextView = findViewById(R.id.player_description)

        playerName.text = items.playerName
        Picasso.get().load(items.playerCutout).into(playerPhoto)
        playerPosition.text = items.playerPosition

        playerDetailName.text = ": " + (items.playerName ?: "-")
        playerDetailPosition.text = ": " + (items.playerPosition ?: "-")
        playerNationality.text = ": " + (items.playerNationality ?: "-")
        playerDateBorn.text = ": " + (items.playerDateBorn ?: "-")
        playerBirthLocation.text = ": " + (items.playerBirthLocation ?: "-")
        playerHeight.text = ": " + (items.playerHeight ?: "-")
        playerWeight.text = ": " + (items.playerWeight ?: "-")
        playerDescription.text = items.playerDescription
        playerDescription.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
