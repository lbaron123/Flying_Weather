package com.lbaron.flyingweather.models

import java.io.Serializable

data class Visibility (
        val repr : String,
        val value : Double,
        val spoken : String
        ) : Serializable