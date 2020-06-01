package com.blazeapps.weatherapp.data.db.unitlocalized.future

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricSimpleFutureWeatherEntry (
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "weather_description")
    override val conditionText: String,
    @ColumnInfo(name = "weather_icon")
    override val conditionIconUrl: String
) : UnitSpecificSimpleFutureWeatherEntry