package com.e.weatherappchallengekt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.weatherappchallengekt.adapter.HourlyAdapter
import com.e.weatherappchallengekt.adapter.OnSwipeTouchListener
import com.e.weatherappchallengekt.model.Daily
import com.e.weatherappchallengekt.model.WeatherInfo
import com.e.weatherappchallengekt.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.text.Typography.degree

class MainActivity : AppCompatActivity() {
    private var mAdapter: HourlyAdapter? = null
    val appPreferences = "AppPreferences"
    private val locationPref = "Locations"
    lateinit var tempUnit:String
    var locations = ArrayList<Triple<Double, Double, String>>()
    lateinit var weatherViewModel:WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempUnit = getSharedPreferences(appPreferences, Context.MODE_PRIVATE)
            .getString("TempUnit", "metric").toString()

        var lat: Double?
        var lon: Double?
        var city: String?
        try {
            val loc = getCurrentCity()
            lon = loc.first
            lat =loc.second
            city = loc.third
            locations.add(Triple(lat!!, lon!!, city!!))
            val stringLocation = getSharedPreferences(appPreferences, Context.MODE_PRIVATE)
                .getString(locationPref, "metric").toString()
            val type = object : TypeToken<List<Triple<Double, Double, String>>?>() {}.getType()
            val savedLocations = Gson().fromJson<List<Triple<Double, Double, String>>>(stringLocation, type)
            locations.addAll(savedLocations)
        } catch (e: Exception) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }


        var locIndex = 0

        base_view.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                if (locIndex != locations.size - 1) {
                    locIndex += 1
                    val (lat, lon, city) = locations[locIndex]
                    retrieveWeather(lat, lon, city, "metric")
                }
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                if (locIndex != 0) {
                    locIndex -= 1
                    val (lat, lon, city) = locations[locIndex]
                    retrieveWeather(lat, lon, city, "metric")
                }
            }
        })

        settings.setOnClickListener {
            var settingsFragment = SettingFragment()
            supportFragmentManager.beginTransaction().add(R.id.base, settingsFragment)
                .addToBackStack("SettingFragment").commit()
        }

        add_location.setOnClickListener {
            var locationFragment = LocationFragment()
            supportFragmentManager.beginTransaction().add(R.id.base, locationFragment)
                .addToBackStack("LocationFragment").commit()
        }

    }

    private fun retrieveWeather(latitude: Double?, longitude: Double?, cityName: String?, units: String){
        var weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        weatherViewModel.init(latitude.toString(), longitude.toString(), units)

        weatherViewModel.isFetchingData().observe(this, Observer { aBool ->
            if (!aBool) {

                weatherViewModel.getWeather()!!.observe(this, Observer { weather: WeatherInfo? ->
                    city_name.text = cityName
                    temp.text = weather?.current?.temp?.let { it.roundToInt().toString() }
                    degree.visibility = View.VISIBLE
                    weather_desc.text = weather?.current?.weather?.get(0)?.description

                    daily_temp.adapter = null
                    mAdapter = weather?.hourly?.let { HourlyAdapter(this, it) }
                    val layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    daily_temp.layoutManager = layoutManager
                    daily_temp.adapter = mAdapter

                    val felike =
                        getString(R.string.feels_like) + " " + weather?.current?.feelsLike?.let { it.roundToInt() }
                    feels_like.text = felike

                    setDailyRecord(weather?.daily!!)

                })


            }
        })
    }

    private fun setDailyRecord(data: List<Daily>) {

        for (weather in data) {
            val dt = Instant.ofEpochSecond(weather.dt!!)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val day = dt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val month = dt.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val date = "$day, $month ${dt.dayOfMonth}"
            val prediction =
                "${weather.temp?.day?.let { it.roundToInt() }}º/${weather.temp?.min?.let { it.roundToInt() }}º"

            when ("$day-$month-${dt.year}") {

                getDate(1) -> {
                    val date = "Tomorrow, $month ${dt.dayOfMonth}"
                    date_1.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_1_image)
                    date_1_weather.text = prediction
                }
                getDate(2) -> {
                    date_2.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_2_image)
                    date_2_weather.text = prediction
                }
                getDate(3) -> {
                    date_3.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_3_image)
                    date_3_weather.text = prediction
                }
                getDate(4) -> {
                    date_4.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_4_image)
                    date_4_weather.text = prediction
                }
                getDate(5) -> {
                    date_5.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_5_image)
                    date_5_weather.text = prediction
                }
                getDate(6) -> {
                    date_6.text = date
                    loadImage(weather.weather?.get(0)?.icon!!, date_6_image)
                    date_6_weather.text = prediction
                }
            }
        }

    }

    private fun getDate(daysFromToday: Int): String {
        val daysInSecs: Long = daysFromToday * 86400000L
        val now =
            Instant.now().plusMillis(daysInSecs).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val day = now.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val month = now.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return "$day-$month-${now.year}"
    }

    private fun loadImage(name: String, view: ImageView) {
        Picasso.get()
            .load("https://openweathermap.org/img/wn/${name}@2x.png")
            .resize(50, 50)
            .centerCrop()
            .into(view)
    }


    private fun getCurrentCity(): Triple<Double?, Double?, String?> {

        var latitude = 0.0
        var longitude = 0.0
        var city: String? = null
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }else{

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            val latitude = location?.latitude
            val longitude = location?.longitude

            val geoCoder = Geocoder(this)
            val addresses: List<Address>

            addresses = geoCoder.getFromLocation(latitude!!, longitude!!, 1)
            city = addresses[0].locality.toString()

            return Triple(longitude, latitude, city)
        }

        return Triple(longitude, latitude, city)
    }


}