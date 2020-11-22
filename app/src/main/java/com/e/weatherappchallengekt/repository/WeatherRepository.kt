package com.e.weatherappchallengekt.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.e.weatherappchallengekt.api.Backend
import com.e.weatherappchallengekt.model.Location
import com.e.weatherappchallengekt.model.WeatherInfo
import kotlinx.coroutines.*

class WeatherRepository {

    private var dataSet: WeatherInfo? = WeatherInfo()
    var isFetching = MutableLiveData<Boolean>()

    fun getLocationWeatherInfo(lat: String, lon: String, metric: String) {
        getWeather(lat, lon, metric)
    }

    fun sendWeather(): MutableLiveData<WeatherInfo> {
        val data = MutableLiveData<WeatherInfo>()
        data.value = dataSet
        return data
    }

    fun getLocation(location: String): MutableLiveData<Location>{
        val data = MutableLiveData<Location>()
        //data.postValue(getLocationCoordinates(location))


        GlobalScope.launch {
            val loc = getLocationCoordinates(location)
            withContext(Dispatchers.Main) {
                data.value = loc
            }
        }


        //data.value = getLocationCoordinates(location)
        return data
    }

    private fun getWeather(lat: String, lon: String, metric: String) {
        isFetching.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataSet = getLocationWeather(lat, lon, metric)
                //Log.d("Print Log", dataSet?.timezone.toString())
            } catch (exception: Exception) {
                Log.d("Spark", exception.toString())
            }
            isFetching.postValue(false)
        }
    }

    private suspend fun getLocationWeather(lat: String, lon: String, units: String): WeatherInfo? {
        var locWeather: WeatherInfo? = null
        CoroutineScope(Dispatchers.IO).launch {
            val response = Backend.getRetrofitApi()!!
                .getWeatherInfo(lat = lat, lon = lon, units = units)
            try {
                if (response?.isSuccessful!!) {
                    locWeather = response.body()!!
                    //Log.d("Print Log", locWeather?.timezone.toString())
                } else
                    Log.d("Print Log", response.message())
            } catch (exception: Exception) {
                Log.d("Spark", exception.toString())
            }
        }.join()
        return locWeather
    }

    private suspend fun getLocationCoordinates(query: String): Location? {
        var location: Location? = null
        CoroutineScope(Dispatchers.IO).launch {
            val response = Backend.getRetrofitApi()!!.getLocation(query)
            try {
                if (response?.isSuccessful!!) {
                    location = response.body()!!
                } else
                    Log.d("Print Log", response.message())
            } catch (exception: Exception) {
                Log.d("Spark", exception.toString())
            }
        }.join()
        return location
    }
}