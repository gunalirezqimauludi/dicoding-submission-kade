package com.gunalirezqi.footballclub.feature.detail.team.main

import com.gunalirezqi.footballclub.model.Team

interface DetailTeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(data: List<Team>)
}