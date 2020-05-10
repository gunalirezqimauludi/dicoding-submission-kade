package com.gunalirezqi.footballclub.model

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("name")
    var standingName: String? = null,

    @SerializedName("teamid")
    var standingTeamId: String? = null,

    @SerializedName("played")
    var standingPlayed: String? = null,

    @SerializedName("goalsfor")
    var standingGoalsFor: String? = null,

    @SerializedName("goalsagainst")
    var standingGoalsAgainst: String? = null,

    @SerializedName("goalsdifference")
    var standingGoalsDifference: String? = null,

    @SerializedName("win")
    var standingWin: String? = null,

    @SerializedName("draw")
    var standingDraw: String? = null,

    @SerializedName("loss")
    var standingLoss: String? = null,

    @SerializedName("total")
    var standingTotal: String? = null
)