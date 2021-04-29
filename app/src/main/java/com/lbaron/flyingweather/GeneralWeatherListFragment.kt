package com.lbaron.flyingweather

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.lbaron.flyingweather.adapters.MetarItemAdapter
import com.lbaron.flyingweather.databaseStuff.Metar
import com.lbaron.flyingweather.databaseStuff.MetarViewModel
import com.lbaron.flyingweather.network.MetarAPIService
import com.lbaron.flyingweather.utility.Constants
import com.lbaron.flyingweather.utility.u
import com.lbaron.flyingweather.weatherModels.MetarResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class GeneralWeatherListFragment : Fragment() {

    private lateinit var mMetarViewModel : MetarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.general_weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        u.l(requireActivity(), "Checking Internet Connected")
        if(u.isNetworkAvailable(requireActivity())){
            u.l(requireActivity(), "Connected to Internet")
        } else {
            u.l(requireActivity(), "No Internet Connection")
        }
        setupRecyclerView()
        // Add airport code
        val fabAddAirport = view.findViewById<FloatingActionButton>(R.id.fab_add_airport)
        fabAddAirport.setOnClickListener(){
            showDialog()
        }
        // Delete Button Code
        val fabDeleteAll = view.findViewById<FloatingActionButton>(R.id.fab_delete_all)
        fabDeleteAll.setOnClickListener(){
            mMetarViewModel.deleteAllMetars()
        }
    }

    /**
     * This will get the metars from the database and show the recyclerview
     */
    private fun setupRecyclerView(){

        u.l(requireActivity().applicationContext, "making dummy recyclerview")

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.metar_recycler_view)
        // Below are the three lines that actually set up the recycler view
        // Adapter takes a lambda function as a parameter - this will go through to what happens onClick
        val adapter = MetarItemAdapter { m: Metar ->
            mMetarViewModel.deleteMetar(m)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext) // Linear layout manager for a vertical scrolling list

        // Metar viewModel initialisation (database stuff)
        mMetarViewModel = ViewModelProvider(this).get(MetarViewModel::class.java)
        // Calling readAllData (database) and making code to run when it is observed -> to the adapter
        mMetarViewModel.readAllData.observe(viewLifecycleOwner, { metars ->
            adapter.setData(metars)
        })
        recyclerView.setHasFixedSize(true)                              // This is a performance optimisation
    }

    /**
     * Gets the METAR of this airport
     * @params airport ICAO eg "EGGD"
     */
    private fun getMetar(airportICAO: String) {
        u.l(requireActivity().applicationContext, "getMetarAsString")
        u.l(requireActivity().applicationContext, "Building Retrofit object")

        // Builds the URL with the baseURL and our MetarAPIService Call
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        u.l(requireActivity().applicationContext, "Retrofit object: $retrofit.")
        // Create an object of the WeatherService that we made
        u.l(requireActivity().applicationContext, "Making the API service")
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
                u.l(requireActivity().applicationContext, "Callback onResponse")
                u.l(requireActivity().applicationContext, "URL: " + call.request().url.toString())
                if (response!!.code() == 200) {
                    u.l(requireActivity().applicationContext, "Response Successful - code ${response.code()}")
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.l(requireActivity().applicationContext, metarResponseJsonString)
                    val metar = Gson().fromJson(metarResponseJsonString, MetarResponse::class.java)
                    u.l(requireActivity().applicationContext, metar.raw)
                    val metarToAdd = Metar(metar.station, metar.raw)
                    u.l(requireActivity().applicationContext, "Adding ${metar.station} to database")
                    mMetarViewModel.addMetar(metarToAdd)


                } else {
                    val rc = response.code()
                    val metarResponse: MetarResponse? = response.body()
                    val metarResponseJsonString = Gson().toJson(metarResponse)
                    u.e(requireActivity().applicationContext, metarResponseJsonString)
                    when (rc) {
                        400 -> u.e(requireActivity().applicationContext, "Error 400 " + "Bad Request - check airport: ${call.request().url}")
                        404 -> u.e(requireActivity().applicationContext, "Error 404 " + "Not found")
                        204 -> u.e(requireActivity().applicationContext, "Error 204 " + "No content - airport doesn't have METAR")
                        else -> u.e(requireActivity().applicationContext, "Error ?? " + "Generic Error")
                    }
                }
            }

            override fun onFailure(call: Call<MetarResponse>, t: Throwable) {
                u.e(requireActivity().applicationContext, "In onFailure")
                u.e(requireActivity().applicationContext, "Url: " + call.request().url.toString())
                u.e(requireActivity().applicationContext, "Error: " + t.message.toString())
                if (t is IOException) {
                    u.e(requireActivity().applicationContext, "This is an IO error")
                }
            }
        })
        u.l(requireActivity().applicationContext, "Finished building the enqueue")
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_airport_dialog)

        // TODO - Do this observe earlier somewhere so it doesn't take so much time
        // TODO - Dialog to a fragment or something
        mMetarViewModel.icaoList.observe(viewLifecycleOwner, {list ->
            val arrayAdapter = ArrayAdapter<String>(requireActivity(), android.R.layout.select_dialog_item, list)
            val etIcaoInput : AutoCompleteTextView = dialog.findViewById(R.id.et_icao_input)

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
        })
    }
}