package com.udacity.asteroidradar.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidRepository
import com.udacity.asteroidradar.database.PictureOfTheDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MainViewModel(
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfTheDayRepository: PictureOfTheDayRepository
) : ViewModel() {

    val getAllAsteroids: LiveData<List<Asteroid>> = asteroidRepository.getAllAsteroids
    val pictureOfTheDay: LiveData<PictureOfDay> = pictureOfTheDayRepository.todayPicture
    private val _currentAsteroids = MutableLiveData<List<Asteroid>>()
    val currentAsteroids: LiveData<List<Asteroid>> get() = _currentAsteroids


    fun refreshAsteroids(startDate: String, endDate: String) {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids(startDate, endDate)
        }
    }

    fun refreshPictureOfTheDay() {
        viewModelScope.launch {
            pictureOfTheDayRepository.refreshPictureOfTheDay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekAsteroids() {
        val today = LocalDate.now()

        val asteroidsLiveData = asteroidRepository.getAsteroidsFromWeek(today.toString())
        asteroidsLiveData.observeForever { asteroidsList ->
            _currentAsteroids.postValue(asteroidsList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayAsteroids() {
        val today = LocalDate.now()

        val asteroidsLiveData = asteroidRepository.getTodayAsteroids(today.toString())
        asteroidsLiveData.observeForever { asteroidsList ->
            _currentAsteroids.postValue(asteroidsList)
        }
    }


    fun getSavedAsteroids() {
        asteroidRepository.getAllAsteroids.observeForever { asteroidsList ->
            _currentAsteroids.postValue(asteroidsList)
        }
    }
}

