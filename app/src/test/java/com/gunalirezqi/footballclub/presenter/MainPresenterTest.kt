package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.League
import com.gunalirezqi.footballclub.model.LeagueResponse
import com.gunalirezqi.footballclub.feature.main.MainActivity
import com.gunalirezqi.footballclub.feature.main.MainPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private lateinit var view: MainActivity

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetLeagues() {
        val league: MutableList<League> = mutableListOf()
        val response = LeagueResponse(league)

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(apiRepository
                    .doRequest(theSportDBApi.getLeagues()).await(),
                    LeagueResponse::class.java
                )
            ).thenReturn(response)

            presenter.getLeague()

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showLeague(league)
            Mockito.verify(view).hideLoading()
        }
    }
}