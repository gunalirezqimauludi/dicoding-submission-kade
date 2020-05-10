package com.gunalirezqi.footballclub.feature.team

import com.gunalirezqi.footballclub.model.Team

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeams(data: List<Team>)
}