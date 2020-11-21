package com.e.weatherappchallengekt

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment() {
    val appPreferences = "AppPreferences"
    val tempPref = "TempUnit"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_header.setOnClickListener {
            activity?.onBackPressed()
        }

        when ((activity as MainActivity).tempUnit) {
            "metric" -> unit_label.text = getString(R.string.celsius)
            "imperial" -> unit_label.text = getString(R.string.fahrenheit)
            "standard" -> unit_label.text = getString(R.string.kelvin)
        }
        temp_container.setOnClickListener { openSortDialog((activity as MainActivity).tempUnit) }

    }

    private fun openSortDialog(tempUnit: String) {
        val activity = activity as MainActivity
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.custom_temp_layout, null)
        val sortGroup = dialogLayout.findViewById<RadioGroup>(R.id.sort_group)
        val celsius = dialogLayout.findViewById<RadioButton>(R.id.celsius)
        val fahrenheit = dialogLayout.findViewById<RadioButton>(R.id.fahrenheit)
        val kelvin = dialogLayout.findViewById<RadioButton>(R.id.kelvin)

        when (tempUnit) {
            "metric" -> celsius.isChecked = true
            "imperial" -> fahrenheit.isChecked = true
            "standard" -> kelvin.isChecked = true
        }

        dialogBuilder.setView(dialogLayout)
        val alertDialog = dialogBuilder.create()

        alertDialog.setOnShowListener {
            celsius.setOnClickListener { activity.tempUnit = "metric"
                unit_label.text = getString(R.string.celsius)
                activity.getSharedPreferences(appPreferences, Context.MODE_PRIVATE).edit()
                    .putString(tempPref, activity.tempUnit).apply()}
            fahrenheit.setOnClickListener { activity.tempUnit = "imperial"
                unit_label.text = getString(R.string.fahrenheit)
                activity.getSharedPreferences(appPreferences, Context.MODE_PRIVATE).edit()
                    .putString(tempPref, activity.tempUnit).apply()}
            kelvin.setOnClickListener { activity.tempUnit = "standard"
                unit_label.text = getString(R.string.kelvin)
                activity.getSharedPreferences(appPreferences, Context.MODE_PRIVATE).edit()
                    .putString(tempPref, activity.tempUnit).apply()}

            alertDialog.setCanceledOnTouchOutside(true)
        }
        alertDialog.show()
    }
}