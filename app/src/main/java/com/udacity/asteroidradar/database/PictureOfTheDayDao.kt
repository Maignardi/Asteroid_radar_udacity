package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface PictureOfTheDayDao {
    @Query("SELECT * FROM pictureFromTheDay LIMIT 1")
    fun getTodayPicture(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(pictureOfDay: PictureOfDay)
}
