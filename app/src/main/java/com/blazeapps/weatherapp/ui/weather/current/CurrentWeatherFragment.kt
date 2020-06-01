package com.blazeapps.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.blazeapps.weatherapp.R
import com.blazeapps.weatherapp.internal.glide.GlideApp
import com.blazeapps.weatherapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }


    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()


        weatherLocation.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDate()
            updateTemperatures(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.conditionIconUrl)
                .into(imageView_condition_icon)

        })
    }

    private fun chooseLocalizedUnitAbbreviation(@StringRes metric: Int, @StringRes imperial: Int): String {
        return if (viewModel.isMetricUnit) getString(metric) else getString(imperial)
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.current_weather_fragment_today)
    }

    private fun updateTemperatures(temperature: Double, feelsLikeTemperature: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(
            R.string.temperature_metric_abbreviation, R.string.temperature_imperial_abbreviation)
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "${getString(R.string.current_weather_fragment_feels_like)} " +
            "$feelsLikeTemperature$unitAbbreviation"

    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(
            R.string.precipitation_metric_abbreviation, R.string.precipitation_imperial_abbreviation)
        textView_precipitation.text = "${getString(R.string.current_weather_fragment_precipitation)} " +
            "$precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(
            R.string.wind_speed_metric_abbreviation, R.string.wind_speed_imperial_abbreviation)
        textView_wind.text = "${getString(R.string.current_weather_fragment_wind)} " +
            "$windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(
            R.string.visibility_metric_abbreviation, R.string.visibility_imperial_abbreviation)
        textView_visibility.text = "${getString(R.string.current_weather_fragment_visibility)} " +
            "$visibilityDistance $unitAbbreviation"
    }

}
