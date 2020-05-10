package com.gunalirezqi.footballclub.adapter.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.gunalirezqi.footballclub.feature.search.event.SearchEventFragment
import com.gunalirezqi.footballclub.feature.search.team.SearchTeamFragment

class SearchPagerAdapter(fm: FragmentManager, keyword: String): FragmentStatePagerAdapter(fm){

    private val pages = listOf(
        SearchEventFragment.newInstance(keyword),
        SearchTeamFragment.newInstance(keyword)
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
            0 -> "Match"
            else -> "Team"
        }
    }
}