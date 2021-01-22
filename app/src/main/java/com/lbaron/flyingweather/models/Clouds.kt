package com.lbaron.flyingweather.models

import java.io.Serializable

class Clouds (
            val repr: String,
            val type: String,
            val altitude: Int,
            val modifier: String,
            val direction: String
        ) : Serializable
