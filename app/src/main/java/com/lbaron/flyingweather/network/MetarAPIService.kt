package com.lbaron.flyingweather.network

import com.lbaron.flyingweather.weatherModels.MetarResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
  * From what I can see, we can make an object of the from retrofit.create<MetarAPIService>(MetarAPIService::class.java)
 * This inherits from Call - so can be called with a queue or enqueue
  */
interface MetarAPIService {
    @GET("metar/{airportICAO}")

    // Call this to generate your service that will then be used by queue or enqueue
    fun getMetarAPIService(
        @Path("airportICAO", encoded = false) airportICAO : String,
        @Query("token") token : String
    ) : Call<MetarResponse> // WeatherResponse is our Java Class containing the weather. A "Call" does the calling and returns what is in the <>

}