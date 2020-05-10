package com.gunalirezqi.footballclub.feature.detail.event

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.DateTime
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class DetailEventActivity : AppCompatActivity(), DetailEventView {

    private lateinit var progressBar: ProgressBar
    lateinit var scrollView: ScrollView

    private lateinit var presenter: DetailEventPresenter
    private lateinit var event: Event

    private lateinit var eventId: String
    private lateinit var eventName: String

    private var menuItem: Menu? = null
    var isFavorite: Boolean = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get Intent Event Value
        eventId = intent.getStringExtra("eventId")
        eventName = intent.getStringExtra("eventName")

        // ActionBar Config
        supportActionBar?.title = eventName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // UI Layout
        relativeLayout {
            linearLayout {
                orientation = LinearLayout.VERTICAL

                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    backgroundColor = Color.parseColor("#008577")
                    padding = dip(16)

                    // Card Event Header
                    cardView {
                        //app:cardCornerRadius = 8dp //not support attribute
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
                    }

                }.lparams(matchParent, wrapContent)

                // Card Event Detail
                scrollView = scrollView {
                    linearLayout {
                        gravity = Gravity.CENTER
                        orientation = LinearLayout.VERTICAL
                        padding = dip(16)

                        cardView {
                            padding = dip(16)

                            linearLayout {
                                orientation = LinearLayout.VERTICAL
                                padding = dip(16)

                                // GoalDetails
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_goal_details
                                        textSize = 12f
                                        gravity = Gravity.START
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_goals)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_goal_details
                                        textSize = 12f
                                        gravity = Gravity.END
                                    }.lparams(matchParent, wrapContent, 1f)
                                }

                                view {
                                    backgroundColor = Color.LTGRAY
                                }.lparams(matchParent, dip(1)) {
                                    topMargin = dip(8)
                                    bottomMargin = dip(8)
                                }

                                // Shots
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_shots
                                        textSize = 12f
                                        gravity = Gravity.START
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_shots)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_shots
                                        textSize = 12f
                                        gravity = Gravity.END
                                    }.lparams(matchParent, wrapContent, 1f)
                                }
                            }
                        }

                        cardView {
                            padding = dip(16)

                            linearLayout {
                                orientation = LinearLayout.VERTICAL
                                padding = dip(16)

                                // Goal Keeper
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_goal_keeper
                                        gravity = Gravity.START
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_goalkeeper)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_goal_keeper
                                        gravity = Gravity.END
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)
                                }

                                view {
                                    backgroundColor = Color.LTGRAY
                                }.lparams(matchParent, dip(1)) {
                                    topMargin = dip(8)
                                    bottomMargin = dip(8)
                                }

                                // Defense
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_defense
                                        gravity = Gravity.START
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_defense)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_defense
                                        gravity = Gravity.END
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)
                                }

                                view {
                                    backgroundColor = Color.LTGRAY
                                }.lparams(matchParent, dip(1)) {
                                    topMargin = dip(8)
                                    bottomMargin = dip(8)
                                }

                                // Midfield
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_midfield
                                        gravity = Gravity.START
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_midfield)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_midfield
                                        gravity = Gravity.END
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)
                                }

                                view {
                                    backgroundColor = Color.LTGRAY
                                }.lparams(matchParent, dip(1)) {
                                    topMargin = dip(8)
                                    bottomMargin = dip(8)
                                }

                                // Forward
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_forward
                                        gravity = Gravity.START
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_forward)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_forward
                                        gravity = Gravity.END
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)
                                }

                                view {
                                    backgroundColor = Color.LTGRAY
                                }.lparams(matchParent, dip(1)) {
                                    topMargin = dip(8)
                                    bottomMargin = dip(8)
                                }

                                // Substitutes
                                linearLayout {
                                    topPadding = dip(8)

                                    textView {
                                        id = R.id.team_home_substitutes
                                        gravity = Gravity.START
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)

                                    textView {
                                        leftPadding = dip(8)
                                        rightPadding = dip(8)
                                        textColor = Color.parseColor("#008577")
                                        text = getString(R.string.label_substitutes)
                                        textSize = 12f
                                    }

                                    textView {
                                        id = R.id.team_away_substitutes
                                        gravity = Gravity.END
                                        textSize = 12f
                                    }.lparams(matchParent, wrapContent, 1f)
                                }
                            }
                        }.lparams {
                            topMargin = dip(16)
                        }

                    }.lparams(matchParent, wrapContent)
                }
            }

            progressBar = progressBar {
            }.lparams {
                centerInParent()
            }
        }

        // Presenter
        val request = ApiRepository()
        val gson = Gson()
        presenter = DetailEventPresenter(this, request, gson)
        presenter.favoriteState(eventId)
        presenter.getEventDetail(eventId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu

        setFavorite()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) presenter.removeFromFavorite(eventId) else presenter.addToFavorite(event)

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun explodeText(text: String?): String? = text?.replace(";", "\n")

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showEventDetail(data: List<Event>) {

        // Get First Data
        val items = data.first()

        // API Repository
        val request = ApiRepository()
        val gson = Gson()

        // Event Date & Time
        val eventDate: TextView = findViewById(R.id.event_date)
        val eventTime: TextView = findViewById(R.id.event_time)
        eventTime.text = items.eventTime?.let { DateTime.getTime(it) }
        eventDate.text = items.eventDate?.let { DateTime.getDate(it) }

        // Home & Away Team Name
        val teamHomeName: TextView = findViewById(R.id.team_home_name)
        val teamAwayName: TextView = findViewById(R.id.team_away_name)
        teamHomeName.text = items.teamHomeName
        teamAwayName.text = items.teamAwayName

        // Home & Away Score
        val teamHomeScore: TextView = findViewById(R.id.team_home_score)
        val teamAwayScore: TextView = findViewById(R.id.team_away_score)
        teamHomeScore.text = (items.teamHomeScore ?: "-")
        teamAwayScore.text = (items.teamAwayScore ?: "-")

        // Home & Away Goal Details
        val teamHomeGoalDetails: TextView = findViewById(R.id.team_home_goal_details)
        val teamAwayGoalDetails: TextView = findViewById(R.id.team_away_goal_details)
        teamHomeGoalDetails.text = explodeText(items.teamHomeGoalDetails)
        teamAwayGoalDetails.text = explodeText(items.teamAwayGoalDetails)

        // Home & Away Shots
        val teamHomeShots: TextView = findViewById(R.id.team_home_shots)
        val teamAwayShots: TextView = findViewById(R.id.team_away_shots)
        teamHomeShots.text = items.teamHomeShots
        teamAwayShots.text = items.teamAwayShots

        // Lineup Goal Keeper
        val teamHomeLineupGoalKeeper: TextView = findViewById(R.id.team_home_goal_keeper)
        val teamAwayLineupGoalKeeper: TextView = findViewById(R.id.team_away_goal_keeper)
        teamHomeLineupGoalKeeper.text = explodeText(items.teamHomeLineupGoalKeeper)
        teamAwayLineupGoalKeeper.text = explodeText(items.teamAwayLineupGoalkeeper)

        // Lineup Midfield
        val teamHomeLineupDefense: TextView = findViewById(R.id.team_home_defense)
        val teamAwayLineupDefense: TextView = findViewById(R.id.team_away_defense)
        teamHomeLineupDefense.text = explodeText(items.teamHomeLineupDefense)
        teamAwayLineupDefense.text = explodeText(items.teamAwayLineupDefense)

        // Lineup Midfield
        val teamHomeLineupMidfield: TextView = findViewById(R.id.team_home_midfield)
        val teamAwayLineupMidfield: TextView = findViewById(R.id.team_away_midfield)
        teamHomeLineupMidfield.text = explodeText(items.teamHomeLineupMidfield)
        teamAwayLineupMidfield.text = explodeText(items.teamAwayLineupMidfield)

        // Lineup Forward
        val teamHomeLineupForward: TextView = findViewById(R.id.team_home_forward)
        val teamAwayLineupForward: TextView = findViewById(R.id.team_away_forward)
        teamHomeLineupForward.text = explodeText(items.teamHomeLineupForward)
        teamAwayLineupForward.text = explodeText(items.teamAwayLineupForward)

        // Lineup Substitutes
        val teamHomeLineupSubstitutes: TextView = findViewById(R.id.team_home_substitutes)
        val teamAwayLineupSubstitutes: TextView = findViewById(R.id.team_away_substitutes)
        teamHomeLineupSubstitutes.text = explodeText(items.teamHomeLineupSubstitutes)
        teamAwayLineupSubstitutes.text = explodeText(items.teamAwayLineupSubstitutes)

        // Home & Away Team Badge
        val teamHomeBadge: ImageView = findViewById(R.id.team_home_badge)
        val teamAwayBadge: ImageView = findViewById(R.id.team_away_badge)

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

            event = Event(
                eventId = items.eventId,
                eventDate = items.eventDate,
                eventTime = items.eventTime,
                eventName = items.eventName,
                teamHomeBadge = teamHome.teams.first().teamBadge,
                teamHomeName = items.teamHomeName,
                teamHomeScore = items.teamHomeScore,
                teamAwayBadge = teamAway.teams.first().teamBadge,
                teamAwayName = items.teamAwayName,
                teamAwayScore = items.teamAwayScore
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
