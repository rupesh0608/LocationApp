package dev.rupeshdeshmukh.locsationapp.ui

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.HiltAndroidApp
import dev.rupeshdeshmukh.locsationapp.databinding.ActivityMainBinding
import dev.rupeshdeshmukh.locsationapp.network.ApiClient
import dev.rupeshdeshmukh.locsationapp.repository.MovieRepository
import dev.rupeshdeshmukh.locsationapp.network.service.MovieApiService
@HiltAndroidApp
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter(emptyList())
        binding.rvMovies.adapter = adapter
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        viewModel.initializeLocation(applicationContext)
        viewModel.locationData.observe(this) { location ->
            updateLocationUI(location)
        }
        viewModel.movies.observe(this) { movies ->
            adapter.setMovies(movies)
        }
        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateLocationUI(location: Location) {

        val lat= "Latitude: ${location.latitude}"
        val long ="Longitude: ${location.longitude}"

        binding.txtLat.text = lat
        binding.txtLong.text = long
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLocationUpdates(this)
    }

}