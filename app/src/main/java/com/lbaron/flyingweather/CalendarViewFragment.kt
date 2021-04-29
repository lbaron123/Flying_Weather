package com.lbaron.flyingweather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbaron.flyingweather.adapters.CalendarItemAdapter
import com.lbaron.flyingweather.utility.u
import me.everything.providers.android.calendar.CalendarProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.BASIC_ISO_DATE
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis


class CalendarViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.calendar_view_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        doCalendarStuff()
    }

    /**
     * Recyclerview for the days and their airports and weathers
     */
    private fun setupRecyclerView(){
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.calendar_recycler_view)
        // Getting the days to put in recyclerview
        // Belopw block generates an array list of 100 days in format - Tuesday 04 June
        val dates = ArrayList<String>()
        val today = LocalDate.now().toString()
        val formatter = DateTimeFormatter.ofPattern("EEEE dd MMM")
        for (i in 0..noDays){
            val day = LocalDate.parse(today).plusDays(i.toLong())
            val dayFormatted = day.format(formatter)
            dates.add(dayFormatted)
        }

        // Below are the three lines that actually set up the recycler view
        val adapter = CalendarItemAdapter(dates)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext) // Linear layout manager for a vertical scrolling list

        recyclerView.setHasFixedSize(true)                              // This is a performance optimisation
    }

    // What happens after we do the permissions dialogue
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_CALENDAR_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // If permission granted by dialogue
                u.l(requireContext().applicationContext, "You have selected to allow access to read_camera")
            } else {
                u.l(requireContext().applicationContext, "You have selected to deny access to read_camera - you can allow this from the settings")
            }
        }
    }

    private fun doCalendarStuff(){
        u.l(requireActivity().applicationContext, "Do calendar stuff")
        // Check if we already have read_calendar permission
        if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            u.l(requireActivity().applicationContext,"You already have permission")
        }else{
            // Request permission - calls onRequestPermissionsResult
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CALENDAR), READ_CALENDAR_PERMISSION_CODE )
        }
        u.l(requireActivity().applicationContext,"We have permission to read calendar")
        val time = measureTimeMillis {
            val calendarProvider = CalendarProvider(requireActivity().applicationContext)
            val events = mutableListOf<CalendarEvent>()
            for (i in 1150..1200){
                val desc: String = calendarProvider.getEvent(i.toLong())?.description?.toString() ?: "t"
                if (desc.length > 2){
                    if(desc.contains("PFO")){
                        val event = CalendarEvent(i.toLong() ,desc)
                        u.l(requireActivity().applicationContext, event.desc)
                        events.add(event)
                    }
                }
            }
        }
        u.l(requireActivity().applicationContext,"Time taken = ${time/1000}")
    }
    companion object{
        private const val READ_CALENDAR_PERMISSION_CODE = 1
        private const val noDays = 100
    }
}