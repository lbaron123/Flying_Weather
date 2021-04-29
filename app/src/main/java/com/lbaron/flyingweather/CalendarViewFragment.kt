package com.lbaron.flyingweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbaron.flyingweather.adapters.CalendarItemAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.BASIC_ISO_DATE
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
import kotlin.collections.ArrayList


class CalendarViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.calendar_view_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    /**
     * Recyclerview for the days and their airports and weathers
     */
    private fun setupRecyclerView(){
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.calendar_recycler_view)
        // Getting the days to put in recyclerview
        // Belopw block generates an array list of 100 days in format - Tuesday 04 June
        val dateTimes = ArrayList<String>()
        val today = LocalDate.now().toString()
        val formatter = DateTimeFormatter.ofPattern("EEEE dd MMM")
        for (i in 0..100){
            val day = LocalDate.parse(today).plusDays(i.toLong())
            val dayFormatted = day.format(formatter)
            dateTimes.add(dayFormatted)
        }

        // Below are the three lines that actually set up the recycler view
        val adapter = CalendarItemAdapter(dateTimes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext) // Linear layout manager for a vertical scrolling list

        recyclerView.setHasFixedSize(true)                              // This is a performance optimisation
    }
}