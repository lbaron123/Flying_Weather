package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class Temperature (
        val repr : String,
        val value : Int,
        val spoken : String
) : Serializable
