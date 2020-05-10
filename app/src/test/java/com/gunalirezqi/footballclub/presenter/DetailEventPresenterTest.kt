package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.detail.event.DetailEventActivity
import com.gunalirezqi.footballclub.feature.detail.event.DetailEventPresenter
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.EventResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailEventPresenterTest {

    @Mock
    private lateinit var view: DetailEventActivity

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: DetailEventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailEventPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetEventDetail() {
        val event: MutableList<Event> = mutableListOf()
        val response = EventResponse(event)
        val leagueId = "4328"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getDetailEvent(leagueId)).await(),
                    EventResponse::class.java
                )
            ).thenReturn(response)

            presenter.getEventDetail(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventDetail(event)
            Mockito.verify(view).hideLoading()
        }
    }
}