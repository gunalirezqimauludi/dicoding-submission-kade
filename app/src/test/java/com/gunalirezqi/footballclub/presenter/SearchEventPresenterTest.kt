package com.gunalirezqi.footballclub.presenter

import com.google.gson.Gson
import com.gunalirezqi.footballclub.TestContextProvider
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.feature.search.event.SearchEventFragment
import com.gunalirezqi.footballclub.feature.search.event.SearchEventPresenter
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.EventSearchResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchEventPresenterTest {

    @Mock
    private lateinit var view: SearchEventFragment

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var theSportDBApi: TheSportDBApi

    private lateinit var eventPresenter: SearchEventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eventPresenter = SearchEventPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun testGetEventSearch() {
        val event: MutableList<Event> = mutableListOf()
        val response = EventSearchResponse(event)
        val keyword = "Arsenal"

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(apiRepository
                    .doRequest(theSportDBApi.getSearchEvents(keyword)).await(),
                    EventSearchResponse::class.java
                )
            ).thenReturn(response)

            eventPresenter.getEventSearch(keyword)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventSearch(event)
            Mockito.verify(view).hideLoading()
        }
    }
}