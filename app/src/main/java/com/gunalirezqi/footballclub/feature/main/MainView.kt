package com.gunalirezqi.footballclub.feature.main

import com.gunalirezqi.footballclub.model.League

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showLeague(data: List<League>)
}