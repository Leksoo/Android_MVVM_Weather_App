package com.blazeapps.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.blazeapps.weatherapp.data.provider.UnitProvider
import com.blazeapps.weatherapp.data.repository.ForecastRepository
import com.blazeapps.weatherapp.internal.UnitSystem
import com.blazeapps.weatherapp.internal.lazyDeferred
import com.resocoder.forecastmvvm.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository,unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetricUnit)
    }

}
