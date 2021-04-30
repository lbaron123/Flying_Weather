package com.lbaron.flyingweather.databaseStuff

import androidx.lifecycle.LiveData

/**
 * Repository class abstracts to multiple data sources - not part of the package but is best practice
  */
class MetarRepository(private val metarDao : MetarDao) {
    val readAllData: LiveData<List<Metar>> = metarDao.readAllData()
    val icaoList: LiveData<List<String>> = metarDao.icaoList()
    val iataList: LiveData<List<String>> = metarDao.iataList()


    suspend fun addMetar(metar: Metar){
        metarDao.addMetar(metar)
    }

    suspend fun deleteMetar(meter: Metar){
        metarDao.deleteMetar(meter)
    }

    suspend fun deleteAllMetars(){
        metarDao.deleteAllMetars()
    }


}