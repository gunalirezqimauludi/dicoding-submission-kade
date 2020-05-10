package com.gunalirezqi.footballclub.model

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("idPlayer")
    var playerId: String? = null,

    @SerializedName("strNationality")
    var playerNationality: String? = null,

    @SerializedName("strPlayer")
    var playerName: String? = null,

    @SerializedName("strDescriptionEN")
    var playerDescription: String? = null,

    @SerializedName("strTeam")
    var playerTeam: String? = null,

    @SerializedName("dateBorn")
    var playerDateBorn: String? = null,

    @SerializedName("strNumber")
    var playerNumber: String? = null,

    @SerializedName("dateSigned")
    var playerSigned: String? = null,

    @SerializedName("strSigning")
    var playerSigning: String? = null,

    @SerializedName("strWage")
    var playerWage: String? = null,

    @SerializedName("strKit")
    var playerKit: String? = null,

    @SerializedName("strBirthLocation")
    var playerBirthLocation: String? = null,

    @SerializedName("strGender")
    var playerGender: String? = null,

    @SerializedName("strSide")
    var playerSide: String? = null,

    @SerializedName("strPosition")
    var playerPosition: String? = null,

    @SerializedName("strHeight")
    var playerHeight: String? = null,

    @SerializedName("strWeight")
    var playerWeight: String? = null,

    @SerializedName("strCutout")
    var playerCutout: String? = null
)