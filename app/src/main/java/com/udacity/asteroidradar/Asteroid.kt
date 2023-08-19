package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import retrofit2.Response

@Parcelize
@Entity(tableName = "asteroid")
data class Asteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

val mockAsteroids: List<Asteroid> = listOf(
    Asteroid(
        id = 1,
        codename = "Asteroid 1",
        closeApproachDate = "2023-08-19",
        absoluteMagnitude = 20.0,
        estimatedDiameter = 100.0,
        relativeVelocity = 50000.0,
        distanceFromEarth = 200000.0,
        isPotentiallyHazardous = true
    ),
    Asteroid(
        id = 2,
        codename = "Asteroid 2",
        closeApproachDate = "2023-08-20",
        absoluteMagnitude = 18.5,
        estimatedDiameter = 150.0,
        relativeVelocity = 60000.0,
        distanceFromEarth = 180000.0,
        isPotentiallyHazardous = false
    )
)

fun mapAsteroidsResponseToAsteroidsList(response: Response<Asteroid>): List<Asteroid> {
    val asteroidResponse = response.body()
    val asteroids = mutableListOf<Asteroid>()

    if (asteroidResponse != null) {
        val asteroid = Asteroid(
            id = asteroidResponse.id,
            codename = asteroidResponse.codename,
            closeApproachDate = asteroidResponse.closeApproachDate,
            absoluteMagnitude = asteroidResponse.absoluteMagnitude,
            estimatedDiameter = asteroidResponse.estimatedDiameter,
            relativeVelocity = asteroidResponse.relativeVelocity,
            distanceFromEarth = asteroidResponse.distanceFromEarth,
            isPotentiallyHazardous = asteroidResponse.isPotentiallyHazardous
        )
        asteroids.add(asteroid)
    }
    return asteroids
}







