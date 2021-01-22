package com.lbaron.flyingweather.models

import java.io.Serializable

data class Time (
        val repr : String,
        val dt : String
) : Serializable
