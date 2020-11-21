package com.e.weatherappchallengekt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.weatherappchallengekt.adapter.LocationAdapter
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {
    private var mAdapter: LocationAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location_list.adapter = null
        mAdapter = LocationAdapter(requireContext(), (activity as MainActivity).locations)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        location_list.layoutManager = layoutManager
        location_list.adapter = mAdapter

        location_header.setOnClickListener {
            activity?.onBackPressed()
        }

        new_location.setOnClickListener {
            var searchFragment = SearchFragment()
            (activity as MainActivity).supportFragmentManager.beginTransaction().add(R.id.base, searchFragment)
                    .addToBackStack("SearchFragment").commit()
        }

    }
}