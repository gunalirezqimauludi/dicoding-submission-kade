package com.gunalirezqi.footballclub.feature.detail.team.player

import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.PlayerResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayerPresenter(
    private val view: PlayerFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPlayer(teamId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getPlayers(teamId)).await(),
                PlayerResponse::class.java
            )

            @Suppress("SENSELESS_COMPARISON")
            if (data.player == null) {
                view.handleNotFound()
            } else {
                view.showPlayer(data.player)
            }
            view.hideLoading()
        }
    }
}