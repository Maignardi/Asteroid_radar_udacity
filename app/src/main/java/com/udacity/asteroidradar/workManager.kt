package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidPictureDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RefreshAsteroidsWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val apiService = NasaApi.retrofitService
        val asteroidDao = AsteroidPictureDatabase.getInstance(applicationContext).asteroidDao()
        val asteroidRepository = AsteroidRepository(asteroidDao, apiService)

        val today = LocalDate.now()
        val endDate = today.plusDays(7)
        return try {
            asteroidRepository.refreshAsteroids(today.toString(), endDate.toString())
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"

        fun setupPeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        }
    }
}
