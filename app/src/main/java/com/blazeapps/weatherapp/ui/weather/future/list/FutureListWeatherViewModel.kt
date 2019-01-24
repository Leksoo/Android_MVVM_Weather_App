package com.blazeapps.weatherapp.ui.weather.future.list

import androidx.lifecycle.ViewModel;
import com.blazeapps.weatherapp.data.provider.UnitProvider
import com.blazeapps.weatherapp.data.repository.ForecastRepository
import com.blazeapps.weatherapp.internal.lazyDeferred
import com.resocoder.forecastmvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), isMetricUnit)
    }
}
