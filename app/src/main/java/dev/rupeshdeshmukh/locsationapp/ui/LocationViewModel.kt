package dev.rupeshdeshmukh.locsationapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rupeshdeshmukh.locsationapp.network.data.Movie
import dev.rupeshdeshmukh.locsationapp.repository.MovieRepository
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    val LOCATION_PERMISSION_REQUEST_CODE = 1001

    init {

        viewModelScope.launch {
            fetchMovies()
        }
    }

    private val _locationData = MutableLiveData<Location>()
    val locationData: LiveData<Location>
        get() = _locationData

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest


   private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                _locationData.postValue(location)
            }
        }
    }

    fun initializeLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    }

    fun startLocationUpdates(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 and higher
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request both permissions if not granted.
                ActivityCompat.requestPermissions(context as Activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE)
                return
            }
        } else {
            // For versions prior to Android 12
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request FINE location permission if not granted.
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun fetchMovies(){
        viewModelScope.launch {
             MovieRepository.getMovies(
                 successHandler = {
                     _movies.value =it.results
                                  },
                 failureHandler = {
                     _movies.value= mutableListOf()
                                  }, {
                  _error.value=it.message
                 })
        }
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
