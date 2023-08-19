package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val apiService: ApiService
) {

    val getAllAsteroids: LiveData<List<Asteroid>> = asteroidDao.getAllAsteroids()

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAsteroids(Constants.API_KEY, startDate, endDate)
                response.body()?.string()?.let {
                    val jsonResult = JSONObject(it)
                    val asteroids = parseAsteroidsJsonResult(jsonResult)

                    withContext(Dispatchers.Default) {
                        asteroidDao.insertAsteroids(asteroids)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun getAsteroidsFromWeek(date: String): LiveData<List<Asteroid>> {
        return asteroidDao.getAsteroidsWeek(date)
    }

    fun getTodayAsteroids(date: String): LiveData<List<Asteroid>> {
        return asteroidDao.getTodayAsteroids(date)
    }

    suspend fun insertAsteroids(asteroids: List<Asteroid>) {
        asteroidDao.insertAsteroids(asteroids)
    }
}

class PictureOfTheDayRepository(
    private val pictureOfTheDayDao: PictureOfTheDayDao,
    private val apiService: ApiService,
    val todayPictureTitle: LiveData<String> = pictureOfTheDayDao.getTodayPicture().map { it.title }
) {

    val todayPicture: LiveData<PictureOfDay> = pictureOfTheDayDao.getTodayPicture()

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val response = apiService.getPictureOfTheDay(Constants.API_KEY)
            response.body()?.let { pictureOfDay ->
                pictureOfTheDayDao.insertPicture(pictureOfDay)
            }
        }
    }
}
