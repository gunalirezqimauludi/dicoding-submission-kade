package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.EventResponse
import com.gunalirezqi.footballclub.feature.event.EventFragment
import com.gunalirezqi.footballclub.feature.event.EventPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EventPreviousPresenterTest {

    @Mock
    private lateinit var view: EventFragment

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: EventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = EventPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetEventPrevious() {
        val event: MutableList<Event> = mutableListOf()
        val response = EventResponse(event)
        val leagueId = "4328"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(apiRepository
                    .doRequest(theSportDBApi.getEventsPrev(leagueId)).await(),
                    EventResponse::class.java
                )
            ).thenReturn(response)

            presenter.getEventPrevious(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventPrevious(event)
            Mockito.verify(view).hideLoading()
        }
    }
}