package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.detail.team.main.DetailTeamActivity
import com.gunalirezqi.footballclub.feature.detail.team.main.DetailTeamPresenter
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailTeamPresenterTest {

    @Mock
    private lateinit var view: DetailTeamActivity

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: DetailTeamPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailTeamPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetTeamDetail() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val teamId = "133604"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getDetailTeam(teamId)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            presenter.getTeamDetail(teamId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamDetail(team)
            Mockito.verify(view).hideLoading()
        }
    }
}