package com.gunalirezqi.footballclub.feature.standing

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.StandingResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StandingPresenter(private val view: StandingFragment,
                        private val apiRepository: ApiRepository,
                        private val gson: Gson,
                        private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getStandings(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getStandings(leagueId)).await(),
                StandingResponse::class.java
            )

            view.showStandings(data.table)
            view.hideLoading()
        }
    }
}