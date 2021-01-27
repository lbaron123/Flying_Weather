package com.lbaron.flyingweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Metar::class], version = 1, exportSchema = false) // Entities are our tables defined by the appropriate class
abstract class MetarDatabase : RoomDatabase() {
    abstract fun metarDao(): MetarDao
    companion object{
        @Volatile
        private var INSTANCE : MetarDatabase? = null    // Create singleton instance of this database - obvs

        /**
         * Return and also sets the database instance
         */
        fun getDatabase(context: Context): MetarDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){ // Apparently synchronised protects it from being access by multiple threads
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MetarDatabase::class.java,
                    "metar_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}