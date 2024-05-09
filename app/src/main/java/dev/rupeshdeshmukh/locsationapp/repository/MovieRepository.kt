package dev.rupeshdeshmukh.locsationapp.repository

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rupeshdeshmukh.locsationapp.network.ApiClient
import dev.rupeshdeshmukh.locsationapp.network.data.Movie
import dev.rupeshdeshmukh.locsationapp.network.data.MovieResponse
import dev.rupeshdeshmukh.locsationapp.network.service.MovieApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@Module
@InstallIn(SingletonComponent::class)
object MovieRepository {
    private val movieService = ApiClient.movieApiService()
     fun getMovies(
        successHandler: (MovieResponse) -> Unit,
        failureHandler: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
         movieService.getMovies().enqueue(object:Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        successHandler(it)
                    }
                }else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    failureHandler(jsonObj.getString("message"))
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}
