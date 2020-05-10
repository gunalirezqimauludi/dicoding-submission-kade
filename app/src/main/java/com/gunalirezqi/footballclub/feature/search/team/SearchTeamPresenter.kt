package com.gunalirezqi.footballclub.feature.search.team

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchTeamPresenter(
    private val view: SearchTeamFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamSearch(keyword: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getSearchTeams(keyword)).await(),
                TeamResponse::class.java
            )

            @Suppress("SENSELESS_COMPARISON")
            if (data.teams == null) {
                view.handleNotFound()
            } else {
                view.showTeamSearch(data.teams)
            }
            view.hideLoading()
        }
    }
}