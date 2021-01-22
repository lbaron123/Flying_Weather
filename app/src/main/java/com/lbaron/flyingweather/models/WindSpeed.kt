package com.lbaron.flyingweather.models

import java.io.Serializable

data class WindSpeed (
        val repr : String,
        val value : Int,
        val spoken : String
) : Serializable
