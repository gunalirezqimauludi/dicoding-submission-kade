package com.gunalirezqi.footballclub.feature.detail.team.player

import com.gunalirezqi.footballclub.model.Player

interface PlayerView {
    fun showLoading()
    fun hideLoading()
    fun showPlayer(data: List<Player>)
}