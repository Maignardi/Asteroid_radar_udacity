package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.RefreshAsteroidsWorker
import com.udacity.asteroidradar.adapter.MainAdapter
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidPictureDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import com.udacity.asteroidradar.database.PictureOfTheDayRepository
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.mockAsteroids

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RefreshAsteroidsWorker.setupPeriodicWork(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val apiService = NasaApi.retrofitService

        val asteroidDao = AsteroidPictureDatabase.getInstance(requireContext()).asteroidDao()
        val pictureOfTheDayDao = AsteroidPictureDatabase.getInstance(requireContext()).pictureOfTheDayDao()

        val asteroidRepository = AsteroidRepository(asteroidDao, apiService)
        val pictureOfTheDayRepository = PictureOfTheDayRepository(pictureOfTheDayDao, apiService)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(asteroidRepository, pictureOfTheDayRepository)
        )[MainViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.refreshPictureOfTheDay()
        viewModel.pictureOfTheDay.observe(viewLifecycleOwner) { picture ->
            Log.d("Picasso", "Observing pictureOfTheDay: $picture")
            picture?.let {
                if (it.mediaType == "image") {
                    Picasso.get()
                        .load(it.url)
                        .into(binding.activityMainImageOfTheDay)
                    Log.d("Picasso", "Carregando imagem da URL: ${it.url}")
                }
            }
        }

        val adapter = MainAdapter()
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.currentAsteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.let {
                adapter.updateAsteroids(it)
            }
        }

        setHasOptionsMenu(true)

        viewModel.getWeekAsteroids()

        pictureOfTheDayRepository.todayPictureTitle.observe(viewLifecycleOwner) { title ->
            binding.activityMainImageOfTheDay.contentDescription = title
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_today_asteroids -> {
                viewModel.getTodayAsteroids()
            }
            R.id.show_saved_asteroids -> {
                viewModel.getSavedAsteroids()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
