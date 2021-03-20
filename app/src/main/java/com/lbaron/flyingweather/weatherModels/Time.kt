package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class Time (
        val repr : String,
        val dt : String
) : Serializable
