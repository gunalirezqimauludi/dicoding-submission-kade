package com.gunalirezqi.footballclub.feature.detail.team.info

import android.os.Build
import android.support.annotation.RequiresApi
import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoPresenter(
    private val view: TeamInfoFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTeamInfo(teamId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailTeam(teamId)).await(),
                TeamResponse::class.java
            )

            view.showTeamInfo(data.teams)
            view.hideLoading()
        }
    }
}