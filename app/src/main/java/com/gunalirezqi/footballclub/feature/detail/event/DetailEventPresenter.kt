package com.gunalirezqi.footballclub.feature.detail.event

import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.database.FavoriteEvent
import com.gunalirezqi.footballclub.database.database
import com.gunalirezqi.footballclub.model.Event
import com.gunalirezqi.footballclub.model.EventResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class DetailEventPresenter(
    private val view: DetailEventActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getEventDetail(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailEvent(leagueId)).await(),
                EventResponse::class.java
            )

            view.showEventDetail(data.events)
            view.hideLoading()
        }
    }

    fun favoriteState(eventId: String) {
        view.database.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
                .whereArgs(
                    "(EVENT_ID = {eventId})",
                    "eventId" to eventId
                )
            val favorite = result.parseList(classParser<FavoriteEvent>())

            view.isFavorite = favorite.isNotEmpty()
        }
    }

    fun addToFavorite(data: Event) {
        try {
            view.database.use {
                insert(
                    FavoriteEvent.TABLE_FAVORITE_EVENT,
                    FavoriteEvent.EVENT_ID to data.eventId,
                    FavoriteEvent.EVENT_DATE to data.eventDate,
                    FavoriteEvent.EVENT_TIME to data.eventTime,
                    FavoriteEvent.EVENT_NAME to data.eventName,
                    FavoriteEvent.EVENT_HOME_BADGE to data.teamHomeBadge,
                    FavoriteEvent.EVENT_HOME_NAME to data.teamHomeName,
                    FavoriteEvent.EVENT_HOME_SCORE to data.teamHomeScore,
                    FavoriteEvent.EVENT_AWAY_BADGE to data.teamAwayBadge,
                    FavoriteEvent.EVENT_AWAY_NAME to data.teamAwayName,
                    FavoriteEvent.EVENT_AWAY_SCORE to data.teamAwayScore
                )
            }

            view.scrollView.snackbar("Added to favorite").show()
        } catch (e: SQLiteConstraintException) {
            view.scrollView.snackbar(e.localizedMessage).show()
        }
    }

    fun removeFromFavorite(eventId: String) {
        try {
            view.database.use {
                delete(
                    FavoriteEvent.TABLE_FAVORITE_EVENT, "(EVENT_ID = {eventId})",
                    "eventId" to eventId
                )
            }

            view.scrollView.snackbar("Removed to favorite").show()
        } catch (e: SQLiteConstraintException) {
            view.scrollView.snackbar(e.localizedMessage).show()
        }
    }
}