package com.lbaron.flyingweather.models

import android.opengl.Visibility
import java.io.Serializable

/**
 *  This Kotlin class is the base class for a Metar Item
  */

data class MetarResponse (
        val meta : Meta,
        val altimeter : Altimeter,
        val clouds : List<Clouds>,
        val flight_rules : String,
        val other : List<String>,
        val sanitized : String,
        val visibility : Visibility,
        val wind_direction : WindDirection,
        val wind_gust : WindGust,
        val wind_speed : WindSpeed,
        val wx_codes : List<WXCodes>,
        val raw : String,
        val station : String,
        val time : Time,
        val remarks : String,
        val remarks_info : RemarksInfo,
        val runwayVisibility : RunwayVisibility,
        val temperature : Temperature,
        val wind_variable_direction : List<WindVariableDirection>,
        //translation?
        val units : Units

        ): Serializable
