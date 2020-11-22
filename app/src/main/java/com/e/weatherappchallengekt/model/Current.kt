package com.e.weatherappchallengekt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Current {

    @SerializedName("dt")
    @Expose
    var dt: Int? = null

    @SerializedName("temp")
    @Expose
    var temp: Double? = null

    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null
}