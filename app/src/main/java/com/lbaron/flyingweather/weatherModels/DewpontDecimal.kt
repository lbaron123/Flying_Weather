package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class DewpontDecimal (
        val repr : String,
        val value : Double,
        val spoken : String
) : Serializable
