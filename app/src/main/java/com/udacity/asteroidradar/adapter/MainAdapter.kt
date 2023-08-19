package com.udacity.asteroidradar.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.main.MainFragmentDirections
import java.time.LocalDate

class MainAdapter : RecyclerView.Adapter<MainAdapter.AsteroidViewHolder>() {

    private var asteroids: List<Asteroid> = listOf()
    private var asteroidsList: List<Asteroid> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AsteroidListItemBinding.inflate(inflater, parent, false)
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = asteroids[position]
        holder.bind(asteroid)
    }

    override fun getItemCount(): Int = asteroids.size

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAsteroids(asteroids: List<Asteroid>) {
        this.asteroids = asteroids
        notifyDataSetChanged()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayAsteroids(asteroidsList: List<Asteroid>): List<Asteroid> {
        val today = LocalDate.now()
        return asteroidsList.filter { asteroid ->
            LocalDate.parse(asteroid.closeApproachDate) == today
        }
    }



    inner class AsteroidViewHolder(private val binding: AsteroidListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val action = MainFragmentDirections.actionShowDetail(asteroids[position])
                    binding.root.findNavController().navigate(action)
                }
            }
        }

        fun bind(asteroid: Asteroid) {
            binding.tvName.text = asteroid.codename
            binding.tvDate.text = asteroid.closeApproachDate
            binding.ivIconHazard.setImageResource(
                if (asteroid.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous
                else R.drawable.ic_status_normal
            )
            if (asteroid.isPotentiallyHazardous) {
                binding.ivIconHazard.contentDescription = "Icon is Pontentially hazardous"
            } else {
                binding.ivIconHazard.contentDescription = "Icon is not hazardous"
            }
        }
    }
}
