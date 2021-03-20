package com.lbaron.flyingweather.metarData

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity make a table
// This is ALSO the class used in the recyclerview
@Entity(tableName = "metar_table")
data class Metar(
    @PrimaryKey(autoGenerate = false)
    val station : String,
    val metar : String //From Raw in our thing
)
