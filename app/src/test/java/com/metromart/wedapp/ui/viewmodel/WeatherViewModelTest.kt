package com.metromart.wedapp.ui.viewmodel

import app.cash.turbine.test
import com.metromart.wedapp.data.model.WeatherResponse
import com.metromart.wedapp.data.remote.NetworkResult
import com.metromart.wedapp.data.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val repository: WeatherRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeather updates uiState to Success when repository returns success`() = runTest {
        // Arrange
        val mockData = mockk<WeatherResponse>()
        every { repository.getWeatherForecast(any(), any()) } returns flowOf(
            NetworkResult.Loading(),
            NetworkResult.Success(mockData)
        )

        // Act
        val viewModel = WeatherViewModel(repository)

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is WeatherUiState.Success)
            assertEquals(mockData, (state as WeatherUiState.Success).data)
        }
    }

    @Test
    fun `fetchWeather updates uiState to Error when repository returns error`() = runTest {
        // Arrange
        val errorMessage = "API Error"
        every { repository.getWeatherForecast(any(), any()) } returns flowOf(
            NetworkResult.Loading(),
            NetworkResult.Error(errorMessage)
        )

        // Act
        val viewModel = WeatherViewModel(repository)

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is WeatherUiState.Error)
            assertEquals(errorMessage, (state as WeatherUiState.Error).message)
            // Verify fallback data is provided on error
            assertTrue(state.fallbackData != null)
        }
    }
}
