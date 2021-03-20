package com.lbaron.flyingweather.weatherModels

import java.io.Serializable

data class RemarksInfo (
        val dewpoint_decimal : DewpontDecimal,
        val temperature_decimal : TemperatureDecimal
) : Serializable
