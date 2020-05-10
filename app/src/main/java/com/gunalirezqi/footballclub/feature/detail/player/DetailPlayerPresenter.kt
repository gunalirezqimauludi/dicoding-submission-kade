package com.gunalirezqi.footballclub.feature.detail.player

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.PlayersResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailPlayerPresenter(
    private val view: DetailPlayerActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    @SuppressLint("NewApi")
    fun getPlayerDetail(playerId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailPlayer(playerId)).await(),
                PlayersResponse::class.java
            )

            view.showPlayerDetail(data.players)
            view.hideLoading()
        }
    }
}