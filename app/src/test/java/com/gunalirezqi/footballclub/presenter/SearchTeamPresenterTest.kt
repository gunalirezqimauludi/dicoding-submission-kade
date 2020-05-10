package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.search.team.SearchTeamFragment
import com.gunalirezqi.footballclub.feature.search.team.SearchTeamPresenter
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchTeamPresenterTest {

    @Mock
    private lateinit var view: SearchTeamFragment

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var eventPresenter: SearchTeamPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eventPresenter = SearchTeamPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetTeamSearch() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val keyword = "Arsenal"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getSearchEvents(keyword)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            eventPresenter.getTeamSearch(keyword)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamSearch(team)
            Mockito.verify(view).hideLoading()
        }
    }
}