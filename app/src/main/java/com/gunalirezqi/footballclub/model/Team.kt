package com.gunalirezqi.footballclub.model

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("idTeam")
    var teamId: String? = null,

    @SerializedName("strTeam")
    var teamName: String? = null,

    @SerializedName("strTeamShort")
    var teamShortName: String? = null,

    @SerializedName("strAlternate")
    var teamAlternateName: String? = null,

    @SerializedName("strDescriptionEN")
    var teamDescription: String? = null,

    @SerializedName("strTeamBadge")
    var teamBadge: String? = null,

    @SerializedName("strStadium")
    var teamStadiumName: String? = null,

    @SerializedName("strStadiumThumb")
    var teamStadiumThumb: String? = null,

    @SerializedName("strStadiumLocation")
    var teamStadiumLocation: String? = null,

    @SerializedName("strStadiumDescription")
    var teamStadiumDescription: String? = null
)