package com.blazeapps.weatherapp.data.db.unitlocalized.current

import androidx.room.ColumnInfo

data class MetricCurrentWeatherEntry(
    @ColumnInfo(name = "tempC")
    override val temperature: Double,

    @ColumnInfo(name = "weather_description")
    override val conditionText: String,

    @ColumnInfo(name = "weather_icon")
    override val conditionIconUrl: String,

    @ColumnInfo(name = "windKph")
    override val windSpeed: Double,

    @ColumnInfo(name = "windDir")
    override val windDirection: String,

    @ColumnInfo(name = "precipMm")
    override val precipitationVolume: Double,

    @ColumnInfo(name = "feelsLikeC")
    override val feelsLikeTemperature: Double,

    @ColumnInfo(name = "visibilityKm")
    override val visibilityDistance: Double
) : UnitSpecificCurrentWeatherEntry

fun MetricCurrentWeatherEntry.toImperial() = ImperialCurrentWeatherEntry(
    temperature = temperature,
    conditionText = conditionText,
    conditionIconUrl = conditionIconUrl,
    windSpeed = windSpeed,
    windDirection = windDirection,
    precipitationVolume = precipitationVolume,
    feelsLikeTemperature = feelsLikeTemperature,
    visibilityDistance = visibilityDistance
)