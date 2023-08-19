package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidPictureDatabase : RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao
    abstract fun pictureOfTheDayDao(): PictureOfTheDayDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidPictureDatabase? = null

        fun getInstance(context: Context): AsteroidPictureDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidPictureDatabase::class.java,
                        "asteroid_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
