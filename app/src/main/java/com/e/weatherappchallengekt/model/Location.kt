package com.e.weatherappchallengekt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("coord")
    @Expose
    var coord: Coord? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    inner class Coord {

        @SerializedName("lon")
        @Expose
        var lon: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

}