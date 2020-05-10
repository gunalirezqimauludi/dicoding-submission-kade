package com.gunalirezqi.footballclub.feature.favorite

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.adapter.fragment.FavoritePagerAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.support.v4.viewPager

class FavoriteActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActionBar Config
        supportActionBar?.title = "Favorite"
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

                                // Tab Fragment
                                cardView {
                                    tabLayout = tabLayout {
                                        lparams(matchParent, wrapContent) {
                                            gravity = Gravity.TOP
                                        }

                                        id = R.id.tabs_favorite
                                    }
                                }
                            }.lparams(matchParent, wrapContent)
                        }
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }.lparams(matchParent, wrapContent)

            nestedScrollView {
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

        // Pager Adapter
        viewPager.adapter = FavoritePagerAdapter(supportFragmentManager)
        viewPager.adapter?.notifyDataSetChanged()
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
