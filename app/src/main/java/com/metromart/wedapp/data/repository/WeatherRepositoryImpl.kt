package com.metromart.wedapp.data.repository

import com.metromart.wedapp.data.model.WeatherResponse
import com.metromart.wedapp.data.remote.NetworkResult
import com.metromart.wedapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getWeatherForecast(lat: Double, lon: Double): Flow<NetworkResult<WeatherResponse>> = flow {
        emit(NetworkResult.Loading())
        val response = remoteDataSource.getForecast(lat, lon)
        emit(response)
    }
}
