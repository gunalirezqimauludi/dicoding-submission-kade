package com.gunalirezqi.footballclub.feature.detail.team.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.fragment.TeamPagerAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.support.v4.viewPager

class DetailTeamActivity : AppCompatActivity(), DetailTeamView {

    private lateinit var progressBar: ProgressBar
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    lateinit var nestedScrollView: NestedScrollView

    private lateinit var presenter: DetailTeamPresenter
    private lateinit var team: Team

    private lateinit var teamId: String
    private lateinit var teamName: String

    private var menuItem: Menu? = null
    var isFavorite: Boolean = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get Intent team Value
        teamId = intent.getStringExtra("teamId")
        teamName = intent.getStringExtra("teamName")

        // ActionBar Config
        supportActionBar?.title = teamName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // UI Layout
        coordinatorLayout {
            fitsSystemWindows = true
            appBarLayout {
                fitsSystemWindows = true

                // Collapsing Appbar
                collapsingToolbarLayout {
                    id = R.id.collapsing_toolbar
                    fitsSystemWindows = true

                    linearLayout {
                        orientation = LinearLayout.VERTICAL

                        relativeLayout {
                            linearLayout {
                                orientation = LinearLayout.VERTICAL
                                backgroundColor = Color.parseColor("#008577")
                                padding = dip(16)

                                // Team Header
                                linearLayout {
                                    orientation = LinearLayout.VERTICAL

                                    imageView {
                                        id = R.id.team_badge
                                    }.lparams(width = dip(100), height = dip(100)) {
                                        gravity = Gravity.CENTER
                                    }
                                    textView {
                                        id = R.id.team_name
                                        gravity = Gravity.CENTER
                                        topPadding = dip(10)
                                        textSize = 25f
                                        textColor = Color.WHITE
                                        setTypeface(typeface, Typeface.BOLD)
                                    }
                                    textView {
                                        id = R.id.team_stadium
                                        gravity = Gravity.CENTER
                                        topPadding = dip(7)
                                        textSize = 14f
                                        textColor = Color.WHITE
                                    }
                                }.lparams(width = matchParent)

                                // Tab Fragment
                                cardView {
                                    tabLayout = tabLayout {
                                        lparams(matchParent, wrapContent) {
                                            gravity = Gravity.TOP
                                        }

                                        id = R.id.tabs_team
                                    }
                                }.lparams(matchParent, wrapContent) {
                                    topMargin = dip(16)
                                }
                            }.lparams(matchParent, wrapContent)

                            progressBar = progressBar {
                            }.lparams {
                                centerInParent()
                            }
                        }
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }.lparams(matchParent, wrapContent)

            nestedScrollView = nestedScrollView {
                isFillViewport = true
                isNestedScrollingEnabled = true
                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    // ViewPager Fragment Content
                    viewPager = viewPager {
                        id = R.id.viewpager_team
                    }.lparams(matchParent, wrapContent)
                }
            }.lparams(width = matchParent, height = matchParent) {
                behavior =
                    Class.forName(resources.getString(R.string.appbar_scrolling_view_behavior)).newInstance() as CoordinatorLayout.Behavior<*>?
            }
        }

        // Presenter
        val request = ApiRepository()
        val gson = Gson()
        presenter = DetailTeamPresenter(this, request, gson)
        presenter.favoriteState(teamId)
        presenter.getTeamDetail(teamId)

        // Pager Adapter
        viewPager.adapter = TeamPagerAdapter(supportFragmentManager, teamId)
        viewPager.adapter?.notifyDataSetChanged()
        tabLayout.setupWithViewPager(viewPager)
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
                if (isFavorite) presenter.removeFromFavorite(teamId) else presenter.addToFavorite(team)

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

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeamDetail(data: List<Team>) {
        // Get First Data
        val items = data.first()

        // Header Info
        val teamName: TextView = findViewById(R.id.team_name)
        val teamBadge: ImageView = findViewById(R.id.team_badge)
        val teamStadium: TextView = findViewById(R.id.team_stadium)

        teamName.text = items.teamName
        Picasso.get().load(items.teamBadge).into(teamBadge)
        teamStadium.text = items.teamStadiumName

        team = Team(
            teamId = items.teamId,
            teamBadge = items.teamBadge,
            teamName = items.teamName,
            teamDescription = items.teamDescription
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
