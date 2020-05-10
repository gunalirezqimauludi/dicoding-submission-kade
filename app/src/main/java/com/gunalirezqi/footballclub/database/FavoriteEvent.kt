package com.gunalirezqi.footballclub.database

data class FavoriteEvent(
    val id: Long?,
    val eventId: String?,
    val eventDate: String?,
    val eventTime: String?,
    val eventName: String?,
    val teamHomeBadge: String?,
    val teamHomeName: String?,
    val teamHomeScore: String?,
    val teamAwayBadge: String?,
    val teamAwayName: String?,
    val teamAwayScore: String?) {
 
    companion object {
        const val TABLE_FAVORITE_EVENT: String = "TABLE_FAVORITE_EVENT"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_DATE: String = "EVENT_DATE"
        const val EVENT_TIME: String = "EVENT_TIME"
        const val EVENT_NAME: String = "EVENT_NAME"
        const val EVENT_HOME_BADGE: String = "EVENT_HOME_BADGE"
        const val EVENT_HOME_NAME: String = "EVENT_HOME_NAME"
        const val EVENT_HOME_SCORE: String = "EVENT_HOME_SCORE"
        const val EVENT_AWAY_BADGE: String = "EVENT_AWAY_BADGE"
        const val EVENT_AWAY_NAME: String = "EVENT_AWAY_NAME"
        const val EVENT_AWAY_SCORE: String = "EVENT_AWAY_SCORE"
    }
}