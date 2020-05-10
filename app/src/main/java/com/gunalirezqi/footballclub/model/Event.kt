package com.gunalirezqi.footballclub.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("idEvent")
    var eventId: String? = null,

    @SerializedName("dateEvent")
    var eventDate: String? = null,

    @SerializedName("strTime")
    var eventTime: String? = null,

    @SerializedName("strEvent")
    var eventName: String? = null,

    var teamHomeBadge: String? = null,

    var teamAwayBadge: String? = null,

    @SerializedName("idHomeTeam")
    var teamHomeId: String? = null,

    @SerializedName("strHomeTeam")
    var teamHomeName: String? = null,

    @SerializedName("intHomeScore")
    var teamHomeScore: String? = null,

    @SerializedName("intHomeShots")
    var teamHomeShots: String? = null,

    @SerializedName("strHomeGoalDetails")
    var teamHomeGoalDetails: String? = null,

    @SerializedName("strHomeLineupGoalkeeper")
    var teamHomeLineupGoalKeeper: String? = null,

    @SerializedName("strHomeLineupDefense")
    var teamHomeLineupDefense: String? = null,

    @SerializedName("strHomeLineupMidfield")
    var teamHomeLineupMidfield: String? = null,

    @SerializedName("strHomeLineupForward")
    var teamHomeLineupForward: String? = null,

    @SerializedName("strHomeLineupSubstitutes")
    var teamHomeLineupSubstitutes: String? = null,

    @SerializedName("idAwayTeam")
    var teamAwayId: String? = null,

    @SerializedName("strAwayTeam")
    var teamAwayName: String? = null,

    @SerializedName("intAwayScore")
    var teamAwayScore: String? = null,

    @SerializedName("intAwayShots")
    var teamAwayShots: String? = null,

    @SerializedName("strAwayGoalDetails")
    var teamAwayGoalDetails: String? = null,

    @SerializedName("strAwayLineupGoalkeeper")
    var teamAwayLineupGoalkeeper: String? = null,

    @SerializedName("strAwayLineupDefense")
    var teamAwayLineupDefense: String? = null,

    @SerializedName("strAwayLineupMidfield")
    var teamAwayLineupMidfield: String? = null,

    @SerializedName("strAwayLineupForward")
    var teamAwayLineupForward: String? = null,

    @SerializedName("strAwayLineupSubstitutes")
    var teamAwayLineupSubstitutes: String? = null
)