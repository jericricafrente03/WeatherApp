package com.metromart.wedapp.data.remote

import com.metromart.wedapp.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "07cdd07390ccbc078cd1c3085bd3e314"
    ): Response<WeatherResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}
