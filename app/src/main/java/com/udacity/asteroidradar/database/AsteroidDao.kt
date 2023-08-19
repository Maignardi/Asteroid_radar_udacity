package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid ORDER BY date(closeApproachDate) ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate >= :date ORDER BY date(closeApproachDate) ASC")
    fun getAsteroidsWeek(date: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate = :date ORDER BY date(closeApproachDate) ASC")
    fun getTodayAsteroids(date: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: List<Asteroid>)
}
