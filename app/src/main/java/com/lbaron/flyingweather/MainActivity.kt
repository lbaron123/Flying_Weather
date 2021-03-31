package com.lbaron.flyingweather

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.lbaron.flyingweather.metarData.Metar
import com.lbaron.flyingweather.metarData.MetarViewModel
import com.lbaron.flyingweather.weatherModels.MetarResponse
import com.lbaron.flyingweather.network.MetarAPIService
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import me.everything.providers.android.calendar.CalendarProvider
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var mMetarViewModel : MetarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        u.l(this, "Checking Internet Connected")
        if(u.isNetworkAvailable(this)){
            u.l(this, "Connected to Internet")
        } else {
            u.l(this, "No Internet Connection")
        }
        // Add airport code
        val fabAddAirport = findViewById<FloatingActionButton>(R.id.fab_add_airport)
        fabAddAirport.setOnClickListener(){
            showDialog()
        }
        // Delete Button Code
        val fabDeleteAll = findViewById<FloatingActionButton>(R.id.fab_delete_all)
        fabDeleteAll.setOnClickListener(){
            mMetarViewModel.deleteAllMetars()
        }
        doCalendarStuff()

    }

    /**
     * Gets the METAR of this airport
     * @params airport ICAO eg "EGGD"
     */
    private fun getMetar(airportICAO: String) {
        u.l(this, "getMetarAsString")
        u.l(this, "Building Retrofit object")

        // Builds the URL with the baseURL and our MetarAPIService Call
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        u.l(this, "Retrofit object: $retrofit.")
        // Create an object of the WeatherService that we made
        u.l(this, "Making the API service")
        val service: MetarAPIService = retrofit.create(MetarAPIService::class.java)
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
                u.l(this@MainActivity, "URL: " + call.request().url.toString())
                if (response!!.isSuccessful) {
                    u.l(this@MainActivity, "Response Successful")
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.l(this@MainActivity, metarResponseJsonString)
                    val metar = Gson().fromJson(metarResponseJsonString, MetarResponse::class.java)
                    u.l(this@MainActivity, metar.raw)
                    val metarToAdd = Metar(metar.station, metar.raw)
                    u.l(this@MainActivity, "Adding ${metar.station} to database")
                    mMetarViewModel.addMetar(metarToAdd)


                } else {
                    val rc = response.code()
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.e(this@MainActivity, metarResponseJsonString)
                    when (rc) {
                        400 -> u.e(this@MainActivity, "Error 400 " + "Bad Request - check airport: ${call.request().url}")
                        404 -> u.e(this@MainActivity, "Error 404 " + "Not found")
                        else -> u.e(this@MainActivity, "Error ?? " + "Generic Error")
                    }
                }
            }

            override fun onFailure(call: Call<MetarResponse>, t: Throwable) {
                u.e(this@MainActivity, "In onFailure")
                u.e(this@MainActivity, "Url: " + call.request().url.toString())
                u.e(this@MainActivity, "Error: " + t.message.toString())
                if (t is IOException) {
                    u.e(this@MainActivity, "This is an IO error")
                }
            }
        })
        u.l(this, "Finished building the enqueue")
    }

    /**
     * This will get the metars from the database and show the recyclerview
     */
    private fun setupRecyclerView(){
        u.l(this, "making dummy recyclerview")
        val recyclerView = findViewById<RecyclerView>(R.id.metar_recycler_view)



        // Below are the three lines that actually set up the recycler view
        // Adapter takes a lambda function as a parameter - this will go through to what happens onClick
        val adapter = MetarItemAdapter { m: Metar ->
            mMetarViewModel.deleteMetar(m)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // Linear layout manager for a vertical scrolling list

        // Metar viewModel initialisation (database stuff)
        mMetarViewModel = ViewModelProvider(this).get(MetarViewModel::class.java)
        // Calling readAllData (database) and making code to run when it is observed -> to the adapter
        mMetarViewModel.readAllData.observe(this, { metars ->
            adapter.setData(metars)
        })
        recyclerView.setHasFixedSize(true)                              // This is a performance optimisation
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_airport_dialog)

        // Code below handles the autocomplete of the dialogue
        //val list = mMetarViewModel.icaoList
        val list = listOf("EGGD", "EGLL","EGKK","EGBB","EGBB","EGPF","EGPH")
        val etIcaoInput : AutoCompleteTextView = dialog.findViewById(R.id.et_icao_input)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, list)
        etIcaoInput.threshold = 1 // Threshold is how many letters need to be written so that the autocomplete happens
        etIcaoInput.setAdapter(arrayAdapter)

        val btnSubmitAirport : Button = dialog.findViewById(R.id.btn_submit_airport)
        btnSubmitAirport.setOnClickListener {
            getMetar(etIcaoInput.text.toString())
            dialog.dismiss()
        }
        // Comment here
        etIcaoInput.requestFocus()
        dialog.show()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }

    private fun doCalendarStuff(){
        u.l(this, "Do calendar stuff")
        runWithPermissions(android.Manifest.permission.READ_CALENDAR){
            u.l(this,"We have permission to read calendar")
            val time = measureTimeMillis {
                val calendarProvider = CalendarProvider(this)
                val events = mutableListOf<CalendarEvent>()
                for (i in 1150..1200){
                    val desc: String = calendarProvider.getEvent(i.toLong())?.description?.toString() ?: "t"
                    if (desc.length > 2){
                        if(desc.contains("PFO")){
                            val event = CalendarEvent(i.toLong() ,desc)
                            u.l(this, event.desc)
                            events.add(event)
                        }
                    }
                }
            }

            u.l(this,"Time taken = ${time/1000}")
        }
    }
}
