package com.gunalirezqi.footballclub.adapter.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.gunalirezqi.footballclub.feature.favorite.event.FavoriteEventFragment
import com.gunalirezqi.footballclub.feature.favorite.team.FavoriteTeamFragment

class FavoritePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val pages = listOf(
        FavoriteEventFragment(),
        FavoriteTeamFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Match"
            else -> "Team"
        }
    }
}