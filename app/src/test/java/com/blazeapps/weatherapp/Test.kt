package com.blazeapps.weatherapp

class Test {

    companion object {
        operator fun invoke() : Test {
            println("+++")
            return Test()
        }
    }
}