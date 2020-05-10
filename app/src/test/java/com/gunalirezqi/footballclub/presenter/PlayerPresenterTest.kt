package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.detail.team.player.PlayerFragment
import com.gunalirezqi.footballclub.feature.detail.team.player.PlayerPresenter
import com.gunalirezqi.footballclub.model.Player
import com.gunalirezqi.footballclub.model.PlayerResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayerPresenterTest {

    @Mock
    private lateinit var view: PlayerFragment

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: PlayerPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PlayerPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetPlayers() {
        val player: MutableList<Player> = mutableListOf()
        val response = PlayerResponse(player)
        val teamId = "133604"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getPlayers(teamId)).await(),
                    PlayerResponse::class.java
                )
            ).thenReturn(response)

            presenter.getPlayer(teamId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showPlayer(player)
            Mockito.verify(view).hideLoading()
        }
    }
}