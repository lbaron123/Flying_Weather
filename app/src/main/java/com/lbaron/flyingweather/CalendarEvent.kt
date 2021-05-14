package com.lbaron.flyingweather

import java.time.Instant
import java.time.LocalDate

data class CalendarEvent(
    val title : String,
    val desc: String,
    val instantStart : Instant,
    val instantFinish: Instant,
    val airport: String
)

// Used to send through the info to the recyclerview
data class DayAndAirport(
        val day : String,
        val airports : ArrayList<String>
)