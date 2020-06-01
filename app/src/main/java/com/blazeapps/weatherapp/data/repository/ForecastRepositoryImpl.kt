package com.blazeapps.weatherapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.blazeapps.weatherapp.data.db.CurrentWeatherDao
import com.blazeapps.weatherapp.data.db.FutureWeatherDao
import com.blazeapps.weatherapp.data.db.WeatherLocationDao
import com.blazeapps.weatherapp.data.db.entity.WeatherLocation
import com.blazeapps.weatherapp.data.db.unitlocalized.current.MetricCurrentWeatherEntry
import com.blazeapps.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.blazeapps.weatherapp.data.db.unitlocalized.current.toImperial
import com.blazeapps.weatherapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.blazeapps.weatherapp.data.network.FORECAST_DAYS_COUNT
import com.blazeapps.weatherapp.data.network.WeatherNetworkDataSource
import com.blazeapps.weatherapp.data.network.response.CurrentWeatherResponse
import com.blazeapps.weatherapp.data.network.response.FutureWeatherResponse
import com.blazeapps.weatherapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }

            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }

    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            val weatherMetric = currentWeatherDao.getWeatherMetric()
            return@withContext if (metric) {
                weatherMetric
            } else {
                Transformations.map(weatherMetric, MetricCurrentWeatherEntry::toImperial)
            }
        }
    }

    override suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {
        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

//        GlobalScope.launch(Dispatchers.IO) {
//            deleteOldForecastData()
//            val futureWeatherList = fetchedWeather.futureWeatherEntries.entries
//            futureWeatherDao.insert(futureWeatherList)
//            weatherLocationDao.upsert(fetchedWeather.location)
//        }

    }


    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
        if (isFetchFutureNeeded()) {
            fetchFutureWeather()
        }
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(), Locale.getDefault().language
        )

    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(), Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT

    }
}