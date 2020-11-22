package com.e.weatherappchallengekt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Temp {

    @SerializedName("day")
    @Expose
    var day: Double? = null

    @SerializedName("min")
    @Expose
    var min: Double? = null

}