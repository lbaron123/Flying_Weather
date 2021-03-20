package com.lbaron.flyingweather.metarData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Metar::class, Airport::class], version = 4, exportSchema = true) // Entities are our tables defined by the appropriate class
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
                )
                        .createFromAsset("database/metar_database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}