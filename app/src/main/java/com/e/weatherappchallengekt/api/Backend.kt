package com.e.weatherappchallengekt.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Backend {

    companion object{
        private const val BASE_URL = "https://api.openweathermap.org"
        private val httpClient= OkHttpClient.Builder().build()

        private val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()

        private val retrofitApi = retrofitBuilder.create(BackendAPI::class.java)

        fun getRetrofitApi(): BackendAPI? {
            return retrofitApi
        }

    }
}