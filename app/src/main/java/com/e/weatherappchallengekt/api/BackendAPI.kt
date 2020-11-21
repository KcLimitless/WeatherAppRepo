package com.e.weatherappchallengekt.api

import android.location.Location
import com.e.weatherappchallengekt.model.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendAPI {

    @GET("/data/2.5/onecall")
    suspend fun getWeatherInfo(@Query("lat") lat:String, @Query("lon") lon:String,
                               @Query("units") units:String,
                               @Query("exclude") exclude:String = "alert,minutely",
                               @Query("appid") appid: String = "da6cd72a7c29df913bdd7ebbeeaf39e7")
            : Response<WeatherInfo?>?


    @GET("/data/2.5/weather")
    suspend fun getLocation(@Query("q") city:String,
                            @Query("appid") appid: String = "da6cd72a7c29df913bdd7ebbeeaf39e7")
            : Response<Location?>?
}