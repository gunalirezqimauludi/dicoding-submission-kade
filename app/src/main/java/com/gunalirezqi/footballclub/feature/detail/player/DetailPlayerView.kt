package com.gunalirezqi.footballclub.feature.detail.player

import com.gunalirezqi.footballclub.model.Player

interface DetailPlayerView {
    fun showLoading()
    fun hideLoading()
    fun showPlayerDetail(data: List<Player>)
}