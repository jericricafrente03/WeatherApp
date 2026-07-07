package com.metromart.wedapp.data.remote

import com.metromart.wedapp.data.model.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getForecast(lat: Double, lon: Double): NetworkResult<WeatherResponse>
}
