package com.e.weatherappchallengekt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.weatherappchallengekt.model.WeatherInfo
import com.e.weatherappchallengekt.repository.WeatherRepository

class WeatherViewModel : ViewModel() {

    private var mRepo: WeatherRepository? = null
    private var weatherDetails: MutableLiveData<WeatherInfo>? = null
    private lateinit var isFetchCompleted: MutableLiveData<Boolean>

    fun init(lat: String, lon: String, units: String) {
        mRepo = WeatherRepository()
        mRepo!!.getLocationWeatherInfo(lat, lon, units)
        isFetchCompleted = mRepo!!.isFetching

    }

    fun getWeather(): LiveData<WeatherInfo>? {
        return mRepo!!.sendWeather()
    }

    val weatherInfo: LiveData<WeatherInfo>?
        get() = weatherDetails

    fun isFetchingData(): LiveData<Boolean> {
        return isFetchCompleted
    }

}