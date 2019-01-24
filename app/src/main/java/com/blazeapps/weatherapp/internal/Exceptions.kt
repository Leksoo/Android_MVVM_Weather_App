package com.blazeapps.weatherapp.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityException : IOException()
class LocationPermissionNotGrantedException: Exception()