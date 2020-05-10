package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.standing.StandingFragment
import com.gunalirezqi.footballclub.feature.standing.StandingPresenter
import com.gunalirezqi.footballclub.model.Standing
import com.gunalirezqi.footballclub.model.StandingResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class StandingPresenterTest {

    @Mock
    private lateinit var view: StandingFragment

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: StandingPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = StandingPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetStandings() {
        val standing: MutableList<Standing> = mutableListOf()
        val response = StandingResponse(standing)
        val leagueId = "4328"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getStandings(leagueId)).await(),
                    StandingResponse::class.java
                )
            ).thenReturn(response)

            presenter.getStandings(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showStandings(standing)
            Mockito.verify(view).hideLoading()
        }
    }
}