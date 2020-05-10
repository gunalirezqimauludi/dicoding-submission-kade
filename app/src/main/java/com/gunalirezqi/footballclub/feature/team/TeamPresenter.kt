package com.gunalirezqi.footballclub.feature.team

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamPresenter(
    private val view: TeamFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeams(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getTeams(leagueId)).await(),
                TeamResponse::class.java
            )

            view.showTeams(data.teams)
            view.hideLoading()
        }
    }
}