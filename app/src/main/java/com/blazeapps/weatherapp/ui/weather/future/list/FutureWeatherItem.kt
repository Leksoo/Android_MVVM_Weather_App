package com.blazeapps.weatherapp.ui.weather.future.list

import android.content.Context
import com.blazeapps.weatherapp.R
import com.blazeapps.weatherapp.data.db.unitlocalized.future.MetricSimpleFutureWeatherEntry
import com.blazeapps.weatherapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.blazeapps.weatherapp.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(
    private val context: Context,
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_future_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry)
            context.getString(R.string.temperature_metric_abbreviation)
        else context.getString(R.string.temperature_imperial_abbreviation)

        textView_temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load(weatherEntry.conditionIconUrl)
            .into(imageView_condition_icon)
    }
}