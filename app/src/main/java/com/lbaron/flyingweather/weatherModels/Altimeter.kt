package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

class Altimeter (
            val repr : String,
            val value : Double,
            val spoken : String
        ) : Serializable
