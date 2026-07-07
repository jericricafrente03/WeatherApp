package com.metromart.wedapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metromart.wedapp.data.model.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CurrentWeatherScreen(weather: WeatherResponse, modifier: Modifier = Modifier) {
    val current = weather.list.first()
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val isNight = hour >= 18 || hour < 6

    val icon = if (current.weather.firstOrNull()?.main == "Rain") {
        Icons.Default.WaterDrop
    } else if (isNight) {
        Icons.Default.Nightlight
    } else {
        Icons.Default.WbSunny
    }

    val dateStr = SimpleDateFormat("EEEE | MMM d", Locale.getDefault()).format(Date(current.dt * 1000))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2E335A))
    ) {
        // Main Weather Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.72f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF5936B4), Color(0xFF362A84))
                    ),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = weather.city.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                            Spacer(Modifier.width(4.dp))
                            Box(modifier = Modifier.size(6.dp).background(Color.White.copy(0.4f), CircleShape))
                            Spacer(Modifier.width(4.dp))
                            Box(modifier = Modifier.size(6.dp).background(Color.White.copy(0.4f), CircleShape))
                        }
                    }
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Big Weather Icon (3D simulation)
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(140.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = dateStr, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                
                Text(
                    text = "${current.main.temp.toInt()}°",
                    fontSize = 90.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = current.weather.firstOrNull()?.main ?: "Heavy rain",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // Details Grid
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DetailItem(Icons.Default.NearMe, "${current.wind.speed} km/h", "Wind")
                    DetailItem(Icons.Default.CloudQueue, "${current.main.humidity}%", "Chance of rain")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DetailItem(Icons.Default.Thermostat, "${current.main.pressure} mbar", "Pressure")
                    DetailItem(Icons.Default.WaterDrop, "${current.main.humidity}%", "Humidity")
                }
            }
        }

        // Bottom Hourly Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.28f)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sunday", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text(text = "Nov 14", color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                items(weather.list.take(12)) { hourData ->
                    HourlyItem(hourData)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Forcats for 7 Days", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.KeyboardDoubleArrowDown, contentDescription = null, tint = Color(0xFF1E88E5), modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherScreenPreview() {
    val dummyWeather = WeatherResponse(
        cod = "200",
        message = 0,
        cnt = 1,
        list = listOf(
            WeatherForecast(
                dt = 1636886400,
                main = MainInfo(
                    temp = 24.0,
                    feelsLike = 26.0,
                    tempMin = 20.0,
                    tempMax = 28.0,
                    pressure = 1010,
                    seaLevel = 1010,
                    grndLevel = 1005,
                    humidity = 83,
                    tempKf = 0.0
                ),
                weather = listOf(
                    WeatherDescription(
                        id = 500,
                        main = "Rain",
                        description = "heavy rain",
                        icon = "10d"
                    )
                ),
                clouds = Clouds(all = 100),
                wind = Wind(speed = 3.7, deg = 200, gust = 5.0),
                visibility = 10000,
                pop = 0.74,
                sys = Sys(pod = "d"),
                dtTxt = "2021-11-14 12:00:00"
            )
        ),
        city = City(
            id = 1,
            name = "Malang",
            coord = Coord(lat = -7.9, lon = 112.6),
            country = "ID",
            population = 800000,
            timezone = 25200,
            sunrise = 1636848000,
            sunset = 1636891200
        )
    )
    CurrentWeatherScreen(weather = dummyWeather)
}

@Composable
fun DetailItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = label, color = Color.White.copy(0.7f), fontSize = 11.sp)
        }
    }
}

@Composable
fun HourlyItem(data: WeatherForecast) {
    val time = try {
        data.dtTxt.substring(11, 16)
    } catch (e: Exception) {
        "Now"
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = time, color = Color.White, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            imageVector = if(data.weather.firstOrNull()?.main == "Rain") Icons.Default.WaterDrop else Icons.Default.WbSunny,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = Color(0xFF1E88E5)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${data.main.tempMin.toInt()}°/${data.main.tempMax.toInt()}°", 
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White
        )
        Text(text = "${data.main.humidity}% rain", color = Color.White, fontSize = 10.sp)
    }
}
