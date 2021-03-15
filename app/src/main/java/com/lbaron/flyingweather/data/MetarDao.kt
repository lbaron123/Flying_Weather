package com.lbaron.flyingweather.data

import androidx.lifecycle.LiveData
import androidx.room.*

// These are the methods used to access the tables
// Dao = data access object
@Dao
interface MetarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)// If identical meter - ignore
    suspend fun addMetar(metar: Metar)

    @Delete
    suspend fun deleteMetar(metar: Metar)

    @Query ("DELETE FROM metar_table")
    suspend fun deleteAllMetars()

    @Query("SELECT * FROM metar_table ORDER BY station ASC")
    fun readAllData():LiveData<List<Metar>>

}