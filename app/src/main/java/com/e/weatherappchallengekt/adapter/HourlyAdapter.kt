package com.e.weatherappchallengekt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.weatherappchallengekt.R
import com.e.weatherappchallengekt.model.Hourly
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.ZoneId
import kotlin.math.roundToInt

class HourlyAdapter  (private val mContext: Context, private val weatherList: List<Hourly>)
    : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.hourly_layout, null)
        return HourlyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class HourlyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var timeView: TextView = view.findViewById(R.id.time)
        var weatherImageView: ImageView = view.findViewById(R.id.time_image)
        var weatherView: TextView = view.findViewById(R.id.time_weather)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val weatherInfo = weatherList[position]
        val dt = Instant.ofEpochSecond(weatherInfo.dt!!)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val time = if(dt.minute < 10)  "${dt.hour}:0${dt.minute}" else "${dt.hour}:${dt.minute}"
        holder.timeView.text = time
        val weather = weatherInfo.temp?.let { it.roundToInt().toString() } + "º"

        Picasso.get()
            .load("https://openweathermap.org/img/wn/${weatherInfo.weather?.get(0)?.icon}@2x.png")
            .resize(50,50)
            .centerCrop()
            .into(holder.weatherImageView)
        holder.weatherView.text = weather

    }
}