package com.gunalirezqi.footballclub.feature.detail.event

import com.gunalirezqi.footballclub.model.Event

interface DetailEventView {
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(data: List<Event>)
}