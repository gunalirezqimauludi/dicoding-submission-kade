package com.gunalirezqi.footballclub.feature.search.event

import com.gunalirezqi.footballclub.model.Team

interface SearchTeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeamSearch(data: List<Team>)
}