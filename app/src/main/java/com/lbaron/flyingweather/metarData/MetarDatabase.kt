package com.lbaron.flyingweather.metarData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Metar::class, Airport::class], version = 6, exportSchema = false) // Entities are our tables defined by the appropriate class
abstract class MetarDatabase : RoomDatabase() {

    abstract fun metarDao(): MetarDao
    companion object{
        @Volatile
        private var INSTANCE : MetarDatabase? = null    // Create singleton instance of this database - obvs

        /**
         * Return and also sets the database instance
         */
        fun getDatabase(context: Context): MetarDatabase{

            val migration1: Migration = object : Migration(5, 6) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Since we didn't alter the table, there's nothing else to do here.
                }
            }
            val migration2: Migration = object : Migration(4, 6) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Since we didn't alter the table, there's nothing else to do here.
                }
            }

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
                        .addMigrations(migration1,migration2)
                        //.fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}