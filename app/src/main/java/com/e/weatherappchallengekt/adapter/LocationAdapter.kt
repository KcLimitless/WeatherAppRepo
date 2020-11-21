package com.e.weatherappchallengekt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.weatherappchallengekt.R

class LocationAdapter (private val mContext: Context, private val locationList: List<Triple<Double, Double, String>>)
    : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.custom_location_layout, null)
        return LocationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    inner class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var locationName: TextView = view.findViewById(R.id.location_name)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val (_,_, city) = locationList[position]
        holder.locationName.text = city
    }

}