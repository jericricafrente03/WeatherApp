package com.metromart.wedapp.data.repository

import com.metromart.wedapp.data.model.WeatherResponse
import com.metromart.wedapp.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecast(lat: Double, lon: Double): Flow<NetworkResult<WeatherResponse>>
}
