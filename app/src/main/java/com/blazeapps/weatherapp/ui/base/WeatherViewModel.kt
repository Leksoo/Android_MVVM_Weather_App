package com.resocoder.forecastmvvm.ui.base

import androidx.lifecycle.ViewModel
import com.blazeapps.weatherapp.data.provider.UnitProvider
import com.blazeapps.weatherapp.data.repository.ForecastRepository
import com.blazeapps.weatherapp.internal.UnitSystem
import com.blazeapps.weatherapp.internal.lazyDeferred


abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModel() {


    val isMetricUnit: Boolean
        get() = unitProvider.getUnitSystem() == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}