package com.blazeapps.weatherapp.data.provider

import com.blazeapps.weatherapp.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}