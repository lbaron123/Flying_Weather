package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class WindGust (
        val repr : String,
        val value : Int,
        val spoken : String
) : Serializable