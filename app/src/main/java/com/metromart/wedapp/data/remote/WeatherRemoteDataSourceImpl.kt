package com.metromart.wedapp.data.remote

import com.metromart.wedapp.data.model.WeatherResponse

class WeatherRemoteDataSourceImpl(
    private val api: WeatherApi
) : BaseDataSource(), WeatherRemoteDataSource {
    override suspend fun getForecast(lat: Double, lon: Double): NetworkResult<WeatherResponse> {
        return safeApiCall { api.getForecast(lat, lon) }
    }
}
