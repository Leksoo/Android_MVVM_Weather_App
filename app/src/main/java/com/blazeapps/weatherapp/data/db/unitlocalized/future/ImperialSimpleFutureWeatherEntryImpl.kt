package com.blazeapps.weatherapp.data.db.unitlocalized.future

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialSimpleFutureWeatherEntry (
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempF")
    override val avgTemperature: Double,
    @ColumnInfo(name = "weather_description")
    override val conditionText: String,
    @ColumnInfo(name = "weather_icon")
    override val conditionIconUrl: String
) : UnitSpecificSimpleFutureWeatherEntry