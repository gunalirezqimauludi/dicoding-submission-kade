package com.gunalirezqi.footballclub.adapter.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.gunalirezqi.footballclub.feature.detail.team.player.PlayerFragment
import com.gunalirezqi.footballclub.feature.detail.team.info.TeamInfoFragment

class TeamPagerAdapter(fm: FragmentManager, teamId: String): FragmentStatePagerAdapter(fm){

    private val pages = listOf(
        TeamInfoFragment.newInstance(teamId),
        PlayerFragment.newInstance(teamId)
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
        return when(position){
            0 -> "Team Info"
            else -> "Player"
        }
    }
}