package com.blazeapps.weatherapp.data.db.entity

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class Condition(
    val code: Int,
    val icon: String,
    val description: String
)

class ConditionDeserializer : JsonDeserializer<Condition> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Condition {
        val jsonObject = json.asJsonObject

        return Condition(
            code = jsonObject["weather_code"].asInt,
            icon = jsonObject["weather_icons"].asJsonArray.get(0).asString,
            description = jsonObject["weather_descriptions"].asJsonArray.get(0).asString
        )
    }
}