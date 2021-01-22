package com.lbaron.flyingweather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lbaron.flyingweather.models.MetarResponse
import com.lbaron.flyingweather.network.MetarAPIService
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        u.l(this, "onCreate")

        u.l(this, "Checking Internet Connected")
        u.l(this, "Git Change")
        if(u.isNetworkAvailable(this)){
            u.l(this, "Connected to Internet")
            u.l(this, "Getting METAR")
            val airport : Array<String> = arrayOf("EGGD", "NZSP", "EGKK", "LFPG","ZZZZ")
            for (item in airport){
                u.l(this, item)
                getMetarAsString(item)
            }
            u.l(this, "Finished metar code")
        } else {
            u.l(this, "No Internet Connection")
        }
    }

    /**
     * Returns the METAR as a string -> Doesn't actually do this
     * @params airport ICAO eg "EGGD"
     */
    private fun getMetarAsString(airportICAO: String) {
        u.l(this, "getMetarAsString")
        u.l(this, "Building Retrofit object")

        //val interceptor = HttpLoggingInterceptor()
        //interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        // Builds the URL with the baseURL and our MetarAPIService Call
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        u.l(this, "Retrofit object: $retrofit.")
        // Create an object of the WeatherService that we made
        u.l(this, "Making the API service")
        val service: MetarAPIService = retrofit.create<MetarAPIService>(MetarAPIService::class.java)
        val listCall: Call<MetarResponse> = service.getMetarAPIService(
                airportICAO,
                Constants.API_KEY
        )

        /**
         * Callback need two members implemented - onResponse and onFailure
         * onResponse takes the input call and output response as parameter
         * If it is successful/fails, hide the progress Dialogue
         */
        listCall.enqueue(object : Callback<MetarResponse> {
            override fun onResponse(call: Call<MetarResponse>, response: Response<MetarResponse>?) {
                u.l(this@MainActivity, "Callback onResponse")
                u.l(this@MainActivity,"URL: " + call.request().url.toString())
                if (response!!.isSuccessful) {
                    u.l(this@MainActivity, "Response Successful")
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.l(this@MainActivity, metarResponseJsonString)
                    val metar = Gson().fromJson(metarResponseJsonString, MetarResponse::class.java)
                    u.l(this@MainActivity, metar.raw)
                    // THis now save the result to preferences
                    // Preference can only take a couple of types of data - string is one and Json is string so that will do

                } else {
                    val rc = response.code()
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.e(this@MainActivity, metarResponseJsonString)
                    when (rc) {
                        400 -> u.e(this@MainActivity,"Error 400 "+ "Bad Request - check airport")
                        404 -> u.e(this@MainActivity,"Error 404 "+ "Not found")
                        else -> u.e(this@MainActivity,"Error ?? "+ "Generic Error")
                    }
                }
            }

            override fun onFailure(call: Call<MetarResponse>, t: Throwable) {
                u.e(this@MainActivity, "In onFailure")
                u.e(this@MainActivity, "Url: " + call.request().url.toString())
                u.e(this@MainActivity, "Error: " + t.message.toString())
                if(t is IOException){
                    u.e(this@MainActivity, "This is an IO error")
                }
            }
        })
        u.l(this, "Finished building the enqueue")
    }
}