package com.lbaron.flyingweather.models

import java.io.Serializable

class Altimeter (
            val repr : String,
            val value : Double,
            val spoken : String
        ) : Serializable
