package com.lbaron.flyingweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lbaron.flyingweather.models.MetarResponse
// Entity make a table
// This is ALSO the class used in the recyclerview
@Entity(tableName = "metar_table")
data class Metar(
    @PrimaryKey(autoGenerate = false)
    val station : String,
    val metar : String //From Raw in our thing
)
