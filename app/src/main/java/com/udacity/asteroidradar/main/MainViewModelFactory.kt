package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidRepository
import com.udacity.asteroidradar.database.PictureOfTheDayRepository

class MainViewModelFactory(
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfTheDayRepository: PictureOfTheDayRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidRepository, pictureOfTheDayRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
