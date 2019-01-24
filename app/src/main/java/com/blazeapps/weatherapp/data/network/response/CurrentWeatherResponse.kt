package com.blazeapps.weatherapp.data.network.response

import com.blazeapps.weatherapp.data.db.entity.CurrentWeatherEntry
import com.blazeapps.weatherapp.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)