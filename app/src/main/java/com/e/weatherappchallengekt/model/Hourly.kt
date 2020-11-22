package com.e.weatherappchallengekt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Hourly {

    @SerializedName("dt")
    @Expose
    var dt: Long? = null

    @SerializedName("temp")
    @Expose
    var temp: Double? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

}