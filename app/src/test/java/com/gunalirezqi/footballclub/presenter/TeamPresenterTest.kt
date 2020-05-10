package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.team.TeamFragment
import com.gunalirezqi.footballclub.feature.team.TeamPresenter
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TeamPresenterTest {

    @Mock
    private lateinit var view: TeamFragment

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: TeamPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetTeams() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val leagueId = "4328"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getTeams(leagueId)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            presenter.getTeams(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeams(team)
            Mockito.verify(view).hideLoading()
        }
    }
}