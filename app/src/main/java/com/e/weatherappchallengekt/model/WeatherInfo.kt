package com.e.weatherappchallengekt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherInfo {

    @SerializedName("current")
    @Expose
    var current: Current? = null

    @SerializedName("hourly")
    @Expose
    var hourly: List<Hourly>? = null

    @SerializedName("daily")
    @Expose
    var daily: List<Daily>? = null
}