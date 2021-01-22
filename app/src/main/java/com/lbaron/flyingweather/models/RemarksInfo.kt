package com.lbaron.flyingweather.models

import java.io.Serializable

data class RemarksInfo (
        val dewpoint_decimal : DewpontDecimal,
        val temperature_decimal : TemperatureDecimal
) : Serializable
