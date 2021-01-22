package com.lbaron.flyingweather.models

import java.io.Serializable

data class WXCodes (
        val repr : String,
        val value : String
) : Serializable
