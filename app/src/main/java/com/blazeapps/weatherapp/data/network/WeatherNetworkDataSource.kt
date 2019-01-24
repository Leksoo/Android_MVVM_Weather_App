package com.blazeapps.weatherapp.data.network

import android.telephony.cdma.CdmaCellLocation
import androidx.lifecycle.LiveData
import com.blazeapps.weatherapp.data.network.response.CurrentWeatherResponse
import com.blazeapps.weatherapp.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(location: String, languageCode: String)
    suspend fun fetchFutureWeather(location: String, languageCode: String)

}