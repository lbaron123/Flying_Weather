package com.lbaron.flyingweather.metarData

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity make a table
// This is ALSO the class used in the recyclerview
@Entity(tableName = "airports2")
data class airports2(
    @PrimaryKey(autoGenerate = false)
    val icao : String,
    val type : String?,
    val name : String?,
    val latitude : Double?,
    val longitude : Double?,
    val elevation : Int?,
    val country : String?,
    val municipality : String?,
    val scheduled : Boolean?,
    val iata : String?
)
