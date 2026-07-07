package com.metromart.wedapp.data.repository

import app.cash.turbine.test
import com.metromart.wedapp.data.model.WeatherResponse
import com.metromart.wedapp.data.remote.NetworkResult
import com.metromart.wedapp.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryTest {

    private val remoteDataSource: WeatherRemoteDataSource = mockk()
    private val repository = WeatherRepositoryImpl(remoteDataSource)

    @Test
    fun `getWeatherForecast emits loading then success`() = runTest {
        // Arrange
        val mockData = mockk<WeatherResponse>()
        coEvery { remoteDataSource.getForecast(any(), any()) } returns NetworkResult.Success(mockData)

        // Act & Assert
        repository.getWeatherForecast(14.5995, 120.9842).test {
            assertTrue(awaitItem() is NetworkResult.Loading)
            val success = awaitItem()
            assertTrue(success is NetworkResult.Success)
            assertEquals(mockData, (success as NetworkResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `getWeatherForecast emits loading then error`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        coEvery { remoteDataSource.getForecast(any(), any()) } returns NetworkResult.Error(errorMessage)

        // Act & Assert
        repository.getWeatherForecast(14.5995, 120.9842).test {
            assertTrue(awaitItem() is NetworkResult.Loading)
            val error = awaitItem()
            assertTrue(error is NetworkResult.Error)
            assertEquals(errorMessage, (error as NetworkResult.Error).message)
            awaitComplete()
        }
    }
}
