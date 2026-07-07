package com.metromart.wedapp.data.remote

import com.metromart.wedapp.data.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class WeatherRemoteDataSourceTest {

    private val api: WeatherApi = mockk()
    private val dataSource = WeatherRemoteDataSourceImpl(api)

    @Test
    fun `getForecast returns success when api returns successful response`() = runTest {
        // Arrange
        val mockResponse = mockk<WeatherResponse>()
        coEvery { api.getForecast(any(), any()) } returns Response.success(mockResponse)

        // Act
        val result = dataSource.getForecast(14.5995, 120.9842)

        // Assert
        assertTrue(result is NetworkResult.Success)
        assertEquals(mockResponse, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getForecast returns error when api returns error response`() = runTest {
        // Arrange
        coEvery { api.getForecast(any(), any()) } returns Response.error(404, mockk(relaxed = true))

        // Act
        val result = dataSource.getForecast(14.5995, 120.9842)

        // Assert
        assertTrue(result is NetworkResult.Error)
        assertTrue(result.message?.contains("404") == true)
    }
}
