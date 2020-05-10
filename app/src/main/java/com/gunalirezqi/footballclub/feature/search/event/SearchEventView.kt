package com.gunalirezqi.footballclub.feature.search.event

import com.gunalirezqi.footballclub.model.Event

interface SearchEventView {
    fun showLoading()
    fun hideLoading()
    fun showEventSearch(data: List<Event>)
}