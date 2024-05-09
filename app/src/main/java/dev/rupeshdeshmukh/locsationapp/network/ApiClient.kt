package dev.rupeshdeshmukh.locsationapp.network

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rupeshdeshmukh.locsationapp.constant.Constant.BASE_URL
import dev.rupeshdeshmukh.locsationapp.network.service.MovieApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    private var mRetrofit: Retrofit

    init {
        val gson = GsonBuilder().setLenient().create()

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getClient())
            .build()
    }

    // Creating OkHttpclient Object
    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
    }

    fun movieApiService(): MovieApiService{
        return mRetrofit.create(MovieApiService::class.java)
    }
}
