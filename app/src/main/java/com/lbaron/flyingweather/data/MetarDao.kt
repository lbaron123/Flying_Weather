package com.lbaron.flyingweather.data

import androidx.lifecycle.LiveData
import androidx.room.*

// These are the methods used to access the tables
// Dao = data access object
@Dao
interface MetarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)// If identical meter - ignore
    suspend fun addMetar(metar: Metar)

    @Query("SELECT * FROM metar_table")
    fun readAllData():LiveData<List<Metar>>

}