package com.blazeapps.weatherapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    @ColumnInfo(name = "weather_code")
    val weatherCode: Int,

    @ColumnInfo(name = "weather_icon")
    val weatherIcon: String,

    @ColumnInfo(name = "weather_description")
    val weatherDescription: String,

    @SerializedName("feelslike")
    val feelsLikeC: Double,

    @SerializedName("precip")
    val precipMm: Double,

    @SerializedName("pressure")
    val pressureMb: Double,

    @SerializedName("temperature")
    val tempC: Double,

    val uv: Double,

    @SerializedName("visibility")
    val visibilityKm: Double,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("wind_speed")
    val windKph: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}

class CurrentWeatherEntryDeserializer : JsonDeserializer<CurrentWeatherEntry> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CurrentWeatherEntry {
        val jsonObject = json.asJsonObject

        return CurrentWeatherEntry(
            weatherCode = jsonObject["weather_code"].asInt,
            weatherIcon = jsonObject["weather_icons"].asJsonArray.get(0).asString,
            weatherDescription = jsonObject["weather_descriptions"].asJsonArray.get(0).asString,
            feelsLikeC = jsonObject["feelslike"].asDouble,
            precipMm = jsonObject["precip"].asDouble,
            pressureMb = jsonObject["pressure"].asDouble,
            tempC = jsonObject["temperature"].asDouble,
            uv = jsonObject["uv_index"].asDouble,
            visibilityKm = jsonObject["visibility"].asDouble,
            windDir = jsonObject["wind_dir"].asString,
            windKph = jsonObject["wind_speed"].asDouble
        )
    }
}