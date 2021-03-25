package com.lbaron.flyingweather.metarData

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity make a table
// This is ALSO the class used in the recyclerview
@Entity(tableName = "airport_table")
data class Airport(
    @PrimaryKey(autoGenerate = false)
    val icao : String,
    val name : String?,
    val municipality : String?,
    val scheduled : Boolean,
    val iata : String?
)