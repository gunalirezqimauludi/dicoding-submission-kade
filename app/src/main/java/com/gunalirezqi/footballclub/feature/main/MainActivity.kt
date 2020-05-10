package com.gunalirezqi.footballclub.feature.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.fragment.MainPagerAdapter
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.feature.favorite.FavoriteActivity
import com.gunalirezqi.footballclub.feature.search.SearchActivity
import com.gunalirezqi.footballclub.model.League
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

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter

    private lateinit var progressBar: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private lateinit var league: League

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            backgroundColor = Color.parseColor("#008577")
                            topPadding = dip(16)
                            leftPadding = dip(16)
                            rightPadding = dip(16)
                            bottomPadding = dip(-14)

                            // Spinner League List
                            relativeLayout {
                                lparams(matchParent, wrapContent)

                                cardView {
                                    spinner = spinner {
                                        id = R.id.spinner
                                    }.lparams(matchParent, wrapContent) {
                                        margin = dip(5)
                                    }
                                }.lparams(matchParent, wrapContent)

                                progressBar = progressBar {
                                }.lparams {
                                    centerInParent()
                                }
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            backgroundColor = Color.parseColor("#008577")
                            leftPadding = dip(16)
                            rightPadding = dip(16)
                            bottomPadding = dip(16)

                            // Card Detail League
                            cardView {
                                relativeLayout {

                                    linearLayout {
                                        orientation = LinearLayout.VERTICAL
                                        frameLayout {
                                            relativeLayout {
                                                imageView {
                                                    id = R.id.league_banner
                                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                                }.lparams(width = matchParent, height = matchParent) {
                                                    centerInParent()
                                                }
                                            }.lparams(height = dip(125))
                                            cardView {
                                                imageView {
                                                    padding = dip(16)
                                                    id = R.id.league_badge
                                                }.lparams(width = dip(100), height = dip(100))
                                            }.lparams {
                                                topMargin = dip(80)
                                                leftMargin = dip(16)
                                            }
                                            linearLayout {
                                                orientation = LinearLayout.VERTICAL
                                                textView {
                                                    id = R.id.league_name
                                                    textSize = 16f //sp
                                                }.lparams(matchParent, wrapContent)
                                                textView {
                                                    id = R.id.league_country
                                                    textSize = 12f //sp
                                                    setTypeface(typeface, Typeface.ITALIC)
                                                }.lparams(matchParent, wrapContent)
                                            }.lparams {
                                                topMargin = dip(140)
                                                leftMargin = dip(135)
                                            }
                                        }.lparams(matchParent, wrapContent)
                                        textView {
                                            padding = dip(16)
                                            id = R.id.league_description
                                            textSize = 12f //sp
                                        }.lparams(matchParent, wrapContent)
                                    }.lparams(matchParent, wrapContent)

                                    progressBar = progressBar {
                                    }.lparams {
                                        centerInParent()
                                    }
                                }
                            }.lparams(matchParent, wrapContent) {
                                topMargin = dip(16)
                            }

                            // Tab Fragment
                            cardView {
                                tabLayout = tabLayout {
                                    lparams(matchParent, wrapContent) {
                                        gravity = Gravity.TOP
                                    }

                                    id = R.id.tabs_main
                                }
                            }.lparams(matchParent, wrapContent) {
                                topMargin = dip(16)
                            }
                        }.lparams(matchParent, wrapContent)
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }.lparams(matchParent, wrapContent)

            nestedScrollView {
                isFillViewport = true
                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    // ViewPager Fragment Content
                    viewPager = viewPager {
                        id = R.id.viewpager_main
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
        presenter = MainPresenter(this, request, gson)
        presenter.getLeague()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        val searchView = menu?.findItem(R.id.action_search)?.actionView as android.support.v7.widget.SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                ComponentName(
                    this,
                    SearchActivity::class.java
                )
            )
        )

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivity<FavoriteActivity>()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLeague(data: List<League>) {

        val leagueBanner: ImageView = findViewById(R.id.league_banner)
        val leagueName: TextView = findViewById(R.id.league_name)
        val leagueCountry: TextView = findViewById(R.id.league_country)
        val leagueDescription: TextView = findViewById(R.id.league_description)
        val leagueBadge: ImageView = findViewById(R.id.league_badge)

        // Adapter
        val spinnerAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, data.map { it.leagueName })
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Trigger Selected Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                league = data.first { it.leagueName == spinner.selectedItem }

                leagueName.text = league.leagueName
                leagueCountry.text = league.leagueCountry
                leagueDescription.text = league.leagueDescription?.substring(0, 200) + "..."
                Picasso.get().load(league.leagueBadge).into(leagueBadge)

                if (league.leagueFanart1 != null) {
                    Picasso.get().load(league.leagueFanart1).into(leagueBanner)
                } else if (league.leagueFanart2 != null) {
                    Picasso.get().load(league.leagueFanart2).into(leagueBanner)
                } else if (league.leagueFanart3 != null) {
                    Picasso.get().load(league.leagueFanart3).into(leagueBanner)
                } else if (league.leagueFanart4 != null) {
                    Picasso.get().load(league.leagueFanart4).into(leagueBanner)
                }

                val leagueId = league.leagueId

                // Pager Adapter
                viewPager.adapter = leagueId?.let {
                    MainPagerAdapter(
                        supportFragmentManager,
                        it
                    )
                }
                viewPager.adapter?.notifyDataSetChanged()
                tabLayout.setupWithViewPager(viewPager)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
