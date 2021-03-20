package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class WXCodes (
        val repr : String,
        val value : String
) : Serializable
