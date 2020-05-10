package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.detail.player.DetailPlayerActivity
import com.gunalirezqi.footballclub.feature.detail.player.DetailPlayerPresenter
import com.gunalirezqi.footballclub.model.Player
import com.gunalirezqi.footballclub.model.PlayersResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailPlayerPresenterTest {

    @Mock
    private lateinit var view: DetailPlayerActivity

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: DetailPlayerPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailPlayerPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetPlayerDetail() {
        val player: MutableList<Player> = mutableListOf()
        val response = PlayersResponse(player)
        val playerId = "34145937"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getDetailPlayer(playerId)).await(),
                    PlayersResponse::class.java
                )
            ).thenReturn(response)

            presenter.getPlayerDetail(playerId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showPlayerDetail(player)
            Mockito.verify(view).hideLoading()
        }
    }
}