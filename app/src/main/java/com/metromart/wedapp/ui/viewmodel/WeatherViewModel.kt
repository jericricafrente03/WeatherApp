package com.metromart.wedapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metromart.wedapp.data.model.WeatherResponse
import com.metromart.wedapp.data.remote.NetworkResult
import com.metromart.wedapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String, val fallbackData: WeatherResponse? = null) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        fetchWeather()
    }

    fun fetchWeather(lat: Double = 14.5995, lon: Double = 120.9842) {
        viewModelScope.launch {
            repository.getWeatherForecast(lat, lon).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value = WeatherUiState.Loading
                    }
                    is NetworkResult.Success -> {
                        result.data?.let {
                            _uiState.value = WeatherUiState.Success(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = WeatherUiState.Error(
                            message = result.message ?: "Unknown error",
                            fallbackData = getStaticWeatherData()
                        )
                    }
                }
            }
        }
    }

    private fun getStaticWeatherData(): com.metromart.wedapp.data.model.WeatherResponse {
        val staticList = listOf(
            com.metromart.wedapp.data.model.WeatherForecast(
                dt = 1783414800,
                main = com.metromart.wedapp.data.model.MainInfo(
                    temp = 33.4, feelsLike = 40.4, tempMin = 32.04, tempMax = 33.4,
                    pressure = 1007, seaLevel = 1007, grndLevel = 1010, humidity = 76,
                    tempKf = 1.36, dewPoint = 28.58
                ),
                weather = listOf(com.metromart.wedapp.data.model.WeatherDescription(500, "Rain", "light rain", "10d")),
                clouds = com.metromart.wedapp.data.model.Clouds(27),
                wind = com.metromart.wedapp.data.model.Wind(4.13, 235, 3.9),
                visibility = 10000, pop = 0.65,
                sys = com.metromart.wedapp.data.model.Sys("d"),
                dtTxt = "2026-07-07 09:00:00"
            ),
            com.metromart.wedapp.data.model.WeatherForecast(
                dt = 1783425600,
                main = com.metromart.wedapp.data.model.MainInfo(
                    temp = 32.1, feelsLike = 39.1, tempMin = 29.49, tempMax = 32.1,
                    pressure = 1007, seaLevel = 1007, grndLevel = 1011, humidity = 76,
                    tempKf = 2.61, dewPoint = 27.33
                ),
                weather = listOf(com.metromart.wedapp.data.model.WeatherDescription(501, "Rain", "moderate rain", "10n")),
                clouds = com.metromart.wedapp.data.model.Clouds(28),
                wind = com.metromart.wedapp.data.model.Wind(2.15, 228, 3.13),
                visibility = 10000, pop = 1.0,
                sys = com.metromart.wedapp.data.model.Sys("n"),
                dtTxt = "2026-07-07 12:00:00"
            )
        )
        return com.metromart.wedapp.data.model.WeatherResponse(
            cod = "200",
            message = 0,
            cnt = 40,
            list = staticList,
            city = com.metromart.wedapp.data.model.City(
                id = 1692184, name = "Quiapo District (Static)",
                coord = com.metromart.wedapp.data.model.Coord(14.5995, 120.9842),
                country = "PH", population = 23138, timezone = 28800,
                sunrise = 1783373522, sunset = 1783420163
            )
        )
    }
}
