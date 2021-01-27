package com.lbaron.flyingweather.data

import androidx.lifecycle.LiveData

/**
 * Repository class abstracts to multiple data sources - not part of the package but is best practice
  */
class MetarRepository(private val metarDao : MetarDao) {
    val readAllData: LiveData<List<Metar>> = metarDao.readAllData()

    suspend fun addMetar(metar: Metar){
        metarDao.addMetar(metar)
    }
}