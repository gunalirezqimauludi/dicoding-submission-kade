package com.gunalirezqi.footballclub.feature.event

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.EventResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventPresenter(
    private val view: EventFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getEventNext(leagueId: String?) {
        view.showLoadingEventNext()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getEventsNext(leagueId)).await(),
                EventResponse::class.java
            )

            @Suppress("SENSELESS_COMPARISON")
            if (data.events == null) {
                view.showNotFoundEventNext()
            } else {
                view.showEventNext(data.events)
            }
            view.hideLoadingEventNext()
        }
    }

    fun getEventPrevious(leagueId: String?) {
        view.showLoadingEventPrevious()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getEventsPrev(leagueId)).await(),
                EventResponse::class.java
            )

            @Suppress("SENSELESS_COMPARISON")
            if (data.events == null) {
                view.showNotFoundEventPrevious()
            } else {
                view.showEventPrevious(data.events)
            }
            view.hideLoadingEventPrevious()
        }
    }
}