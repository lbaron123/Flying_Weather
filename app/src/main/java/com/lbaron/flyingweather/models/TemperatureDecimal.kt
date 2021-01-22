package com.lbaron.flyingweather.models

import java.io.Serializable

data class TemperatureDecimal (
        val repr : String,
        val value : Double,
        val spoken : String
) : Serializable
