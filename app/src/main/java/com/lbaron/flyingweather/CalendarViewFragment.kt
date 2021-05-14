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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbaron.flyingweather.adapters.CalendarItemAdapter
import com.lbaron.flyingweather.databaseStuff.MetarViewModel
import com.lbaron.flyingweather.utility.u
import me.everything.providers.android.calendar.CalendarProvider
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class CalendarViewFragment : Fragment() {

    private lateinit var mMetarViewModel : MetarViewModel
    private var dayAndAirports : ArrayList<DayAndAirport> = arrayListOf()
    companion object{
        private const val READ_CALENDAR_PERMISSION_CODE = 1
        private const val noDays = 100
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Keeps a list of the iataCodes for use later in the calendar stuff
        mMetarViewModel = ViewModelProvider(this).get(MetarViewModel::class.java)
            return inflater.inflate(R.layout.calendar_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doCalendarStuff()
        setupRecyclerView()
    }

    /**
     * Recyclerview for the days and their airports and weathers
     */
    private fun setupRecyclerView(){
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.calendar_recycler_view)
        // Getting the days to put in recyclerview
        // Below block generates an array list of noDays days in format - Tuesday 04 June
        // TODO - ensure that timezones are used correctly here
        val formatter = DateTimeFormatter.ofPattern("EEEE dd MMM")
        for (i in 0..noDays){
            val day = LocalDate.now().plusDays(i.toLong())
            val airports = ArrayList<String>()
            airports.add("BRS")
            airports.add("CDG")

            val dayFormatted = day.format(formatter)
            dayAndAirports.add(DayAndAirport(dayFormatted, airports))

        }

        // Below are the three lines that actually set up the recycler view
        val adapter = CalendarItemAdapter(dayAndAirports)
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
        // TODO put this content in a refresh button perhaps
        u.l(requireActivity().applicationContext, "Do calendar stuff")
        // Check if we already have read_calendar permission
        if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            u.l(requireActivity().applicationContext,"You already have permission")
        }else{
            // Request permission - calls onRequestPermissionsResult
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CALENDAR), READ_CALENDAR_PERMISSION_CODE )
            // TODO what to do if it is rejected
        }
        u.l(requireActivity().applicationContext,"We have permission to read calendar")


        mMetarViewModel.iataList.observe(viewLifecycleOwner, Observer { iataCodes ->

        val calendarProvider = CalendarProvider(requireActivity().applicationContext)
        val events = mutableListOf<CalendarEvent>()
        val instantStartOfToday = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
        val instantStartOfMaxDayInFuture = LocalDate.now().plusDays(noDays.toLong()).atStartOfDay().toInstant(ZoneOffset.UTC)
        for (calendar in calendarProvider.calendars.list){
            // Go through each calendar
            u.l(requireContext().applicationContext, "Looking at calendar ${calendar.displayName}")
            for (event in calendarProvider.getEvents(calendar.id).list){
                //Events in a calendar
                val instantEventStartTime = Instant.ofEpochMilli(event.dTStart)
                if(instantEventStartTime.isAfter(instantStartOfToday) && instantEventStartTime.isBefore(instantStartOfMaxDayInFuture) && event.title != null){
                    for (airportIATA in iataCodes){
                        if  (event.title.contains(airportIATA)){
                            u.l(requireContext().applicationContext, "Event ${event.title} starting on ${Instant.ofEpochMilli(event.dTStart)} has $airportIATA in title")
                            events.add(CalendarEvent(event.title, event.description, instantEventStartTime, Instant.ofEpochMilli(event.dTend), airportIATA))
                        }
                    }
                }
            }
        }
            // Makes a map - key = LocalDate eg 2020-03-01, value is list of events that happen in that date
           val dateEventMap = events.groupBy { LocalDateTime.ofInstant(it.instantStart, ZoneId.systemDefault()).toLocalDate()}
        })

    }

}