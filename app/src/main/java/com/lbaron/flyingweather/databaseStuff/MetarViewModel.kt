package com.lbaron.flyingweather.databaseStuff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel is to provide data to the UI and survive configuration changes.
 * A ViewModel acts as a communication center between the repository and the UI
 */
class MetarViewModel(application: Application) : AndroidViewModel(application){
    val readAllData : LiveData<List<Metar>>
    private val repository: MetarRepository
    val icaoList : LiveData<List<String>>
    val iataList : LiveData<List<String>>


    init {
        val metarDao = MetarDatabase.getDatabase(application).metarDao()
        repository = MetarRepository(metarDao)
        readAllData = repository.readAllData
        icaoList = repository.icaoList
        iataList = repository.iataList


    }

    /**
     * Calls addMetar in repository, then addMetar in MetarDao - 2 layers of abstraction
     */
    fun addMetar(metar: Metar){
        // viewModelScope from kotlin coroutines - this is multithreaded
        viewModelScope.launch(Dispatchers.IO){
            repository.addMetar(metar)
        }
    }

    fun deleteMetar(metar: Metar){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteMetar(metar)
        }
    }

    fun deleteAllMetars(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllMetars()
        }
    }

}