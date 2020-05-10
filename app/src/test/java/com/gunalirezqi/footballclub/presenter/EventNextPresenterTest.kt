package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.event.next.EventNextFragment
import com.gunalirezqi.footballclub.feature.event.next.EventNextPresenter
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.EventResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EventNextPresenterTest {

    @Mock
    private lateinit var view: EventNextFragment

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var presenter: EventNextPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = EventNextPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetEventNext() {
        val event: MutableList<Event> = mutableListOf()
        val response = EventResponse(event)
        val leagueId = "4328"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(theSportDBApi.getEventsNext(leagueId)).await(),
                    EventResponse::class.java
                )
            ).thenReturn(response)

            presenter.getEventNext(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventNext(event)
            Mockito.verify(view).hideLoading()
        }
    }
}