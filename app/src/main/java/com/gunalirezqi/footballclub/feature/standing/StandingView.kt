package com.gunalirezqi.footballclub.feature.standing

import com.gunalirezqi.footballclub.model.Standing

interface StandingView {
    fun showLoading()
    fun hideLoading()
    fun showStandings(data: List<Standing>)
}