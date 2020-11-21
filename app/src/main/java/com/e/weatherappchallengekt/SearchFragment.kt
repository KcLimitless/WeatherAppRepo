package com.e.weatherappchallengekt

import android.content.Context

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.e.weatherappchallengekt.model.Location
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private val appPreferences = "AppPreferences"
    private val locationPref = "Locations"

    private var newLocation:Triple<Double, Double, String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = (activity as MainActivity)

        searchLocation.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                activity.weatherViewModel.getLocation(searchLocation.text.toString())!!
                        .observe(viewLifecycleOwner, Observer { location: Location? ->
                            if (location != null){
                                newLocation = Triple(location.coord?.lat!!, location.coord?.lon!!, location.name!!)
                                Toast.makeText(requireContext(), location.name+" Available", Toast.LENGTH_SHORT).show()
                                confirm.isEnabled = true
                            }
                            else {
                                confirm.isEnabled = false
                                newLocation = null
                                Toast.makeText(requireContext(), "Location Not Found", Toast.LENGTH_SHORT).show()
                            }
                        })
                true
            }
            false
        }

        confirm.setOnClickListener {
            if (newLocation != null){
                for(loc in activity.locations){
                    if(loc.third == newLocation!!.third) {
                        Toast.makeText(requireContext(), "Location already added", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                activity.locations.add(newLocation!!)
                Toast.makeText(requireContext(), "Location Added", Toast.LENGTH_SHORT).show()

                val gSon = Gson()
                val locationsToSave = activity.locations.drop(1)
                val json = gSon.toJson(locationsToSave)

                activity.getSharedPreferences(appPreferences, Context.MODE_PRIVATE).edit()
                    .putString(locationPref, json).apply()
                confirm.isEnabled = false
            }
        }

    }
}


