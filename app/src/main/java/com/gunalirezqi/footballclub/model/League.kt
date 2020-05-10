package com.gunalirezqi.footballclub.model

import com.google.gson.annotations.SerializedName

data class League(
    @SerializedName("idLeague")
    var leagueId: String? = null,

    @SerializedName("strLeague")
    var leagueName: String? = null,

    @SerializedName("strCountry")
    var leagueCountry: String? = null,

    @SerializedName("strDescriptionEN")
    var leagueDescription: String? = null,

    @SerializedName("strBadge")
    var leagueBadge: String? = null,

    @SerializedName("strFanart1")
    var leagueFanart1: String? = null,

    @SerializedName("strFanart2")
    var leagueFanart2: String? = null,

    @SerializedName("strFanart3")
    var leagueFanart3: String? = null,

    @SerializedName("strFanart4")
    var leagueFanart4: String? = null
)