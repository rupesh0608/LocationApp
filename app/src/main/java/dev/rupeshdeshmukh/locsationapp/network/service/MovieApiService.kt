package dev.rupeshdeshmukh.locsationapp.network.service

import dev.rupeshdeshmukh.locsationapp.constant.Constant.API_KEY
import dev.rupeshdeshmukh.locsationapp.network.data.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiService {

    @GET("movie/popular?api_key=$API_KEY")
   fun getMovies(): Call<MovieResponse>
}