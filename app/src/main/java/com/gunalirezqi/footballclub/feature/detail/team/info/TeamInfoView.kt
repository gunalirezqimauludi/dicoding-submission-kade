package com.gunalirezqi.footballclub.feature.detail.team.info

import com.gunalirezqi.footballclub.model.Team

interface TeamInfoView {
    fun showLoading()
    fun hideLoading()
    fun showTeamInfo(data: List<Team>)
}