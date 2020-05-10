package com.gunalirezqi.footballclub.feature.search.event

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.EventSearchResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchEventPresenter(
    private val view: SearchEventFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getEventSearch(keyword: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getSearchEvents(keyword)).await(),
                EventSearchResponse::class.java
            )

            @Suppress("SENSELESS_COMPARISON")
            if (data.event == null) {
                view.handleNotFound()
            } else {
                view.showEventSearch(data.event)
            }
            view.hideLoading()
        }
    }
}