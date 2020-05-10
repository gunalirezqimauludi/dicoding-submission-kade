package com.gunalirezqi.footballclub.adapter.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.gunalirezqi.footballclub.feature.event.EventFragment
import com.gunalirezqi.footballclub.feature.standing.StandingFragment
import com.gunalirezqi.footballclub.feature.team.TeamFragment

class MainPagerAdapter(fm: FragmentManager, leagueId: String) : FragmentStatePagerAdapter(fm) {

    private val pages = listOf(
        EventFragment.newInstance(leagueId),
        StandingFragment.newInstance(leagueId),
        TeamFragment.newInstance(leagueId)
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
            1 -> "Standings"
            else -> "Team"
        }
    }
}