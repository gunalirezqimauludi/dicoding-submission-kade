package com.gunalirezqi.footballclub.feature.detail.team.main

import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.api.TheSportDBApi
import com.gunalirezqi.footballclub.database.FavoriteTeam
import com.gunalirezqi.footballclub.database.database
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.model.TeamResponse
import com.gunalirezqi.footballclub.utils.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class DetailTeamPresenter(
    private val view: DetailTeamActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamDetail(teamId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailTeam(teamId)).await(),
                TeamResponse::class.java
            )

            view.showTeamDetail(data.teams)
            view.hideLoading()
        }
    }

    fun favoriteState(teamId: String) {
        view.database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                .whereArgs(
                    "(TEAM_ID = {teamId})",
                    "teamId" to teamId
                )
            val favorite = result.parseList(classParser<FavoriteTeam>())

            view.isFavorite = favorite.isNotEmpty()
        }
    }

    fun addToFavorite(data: Team) {
        try {
            view.database.use {
                insert(
                    FavoriteTeam.TABLE_FAVORITE_TEAM,
                    FavoriteTeam.TEAM_ID to data.teamId,
                    FavoriteTeam.TEAM_BADGE to data.teamBadge,
                    FavoriteTeam.TEAM_NAME to data.teamName,
                    FavoriteTeam.TEAM_DESCRIPTION to data.teamDescription
                )
            }

            view.nestedScrollView.snackbar("Added to favorite").show()
        } catch (e: SQLiteConstraintException) {
            view.nestedScrollView.snackbar(e.localizedMessage).show()
        }
    }

    fun removeFromFavorite(teamId: String) {
        try {
            view.database.use {
                delete(
                    FavoriteTeam.TABLE_FAVORITE_TEAM, "(TEAM_ID = {teamId})",
                    "teamId" to teamId
                )
            }

            view.nestedScrollView.snackbar("Removed to favorite").show()
        } catch (e: SQLiteConstraintException) {
            view.nestedScrollView.snackbar(e.localizedMessage).show()
        }
    }
}