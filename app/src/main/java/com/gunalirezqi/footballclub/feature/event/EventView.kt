package com.gunalirezqi.footballclub.feature.event

import com.gunalirezqi.footballclub.model.Event

interface EventView {
    fun showLoadingEventPrevious()
    fun showLoadingEventNext()
    fun hideLoadingEventPrevious()
    fun hideLoadingEventNext()
    fun showEventPrevious(data: List<Event>)
    fun showEventNext(data: List<Event>)
}