package com.freezer.android_course_week_6.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(FavFilmEntity::class),version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavFilmDatabase : RoomDatabase() {
    abstract fun favFilmDao(): FavFilmDao

    companion object{
        @Volatile
        private var INSTANCE: FavFilmDatabase? = null
        fun getDataBase(context: Context): FavFilmDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    FavFilmDatabase::class.java,
                    "fav_film_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}