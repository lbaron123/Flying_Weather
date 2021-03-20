package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class Units (
        val altimeter : String,
        val altitude : String,
        val temperature : String,
        val visibility : String,
        val wind_speed : String
) : Serializable
