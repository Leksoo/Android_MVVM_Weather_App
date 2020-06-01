package com.blazeapps.weatherapp.data.network

import com.blazeapps.weatherapp.data.network.response.CurrentWeatherResponse
import com.blazeapps.weatherapp.data.network.response.FutureWeatherResponse
import com.blazeapps.weatherapp.data.network.response.key
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.apixu.com/v1/current.json?key=08908e4f9f4e4f00928114837192001&q=Paris
const val BASE_URL = "http://api.weatherstack.com/"

interface ApixuWeatherApiService {

    @GET("current")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast")
    fun getFutureWeather(
        @Query("q") location: String,
        @Query("days") days:Int,
        @Query("lang") languageCode: String = "en"
    ): Deferred<FutureWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request().url().newBuilder()
                    .addQueryParameter("access_key", key).build()
                val request = chain.request().newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()
            return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApixuWeatherApiService::class.java)
        }
    }
}