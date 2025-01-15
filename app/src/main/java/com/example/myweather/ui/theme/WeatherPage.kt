package com.example.myweather.ui.theme

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.myweather.R
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.WeatherData
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun WeatherPage(viewModel: WeatherViewModel) {


    var city by remember { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var backgroundImageRes by remember { mutableStateOf(R.drawable.clear) }
    Image(
        painter = painterResource(id = backgroundImageRes),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text("Search for any location", color = Color.Black) },

                )

            IconButton(onClick = {
                viewModel.getData(city)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location"
                )
            }
        }
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = "Error: ${result.message}")
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()

            }

            is NetworkResponse.Success -> {
                val condition = result.data.current.condition.text
                backgroundImageRes = when (condition.lowercase()) {
                    "sunny" -> R.drawable.sunny
                    "overcast" -> R.drawable.overcast
                    "rainy" -> R.drawable.rain
                    "cloudy" -> R.drawable.mist
                    "mist" -> R.drawable.mist
                    "fog" -> R.drawable.mist
                    "light snow" -> R.drawable.snow
                    "snow" -> R.drawable.snow
                    "partly cloudy" -> R.drawable.partyly
                    "patchy rain nearby" -> R.drawable.rain
                    "light rain shower" -> R.drawable.rain
                    "light drizzle" -> R.drawable.rain
                    "light rain" -> R.drawable.rain
                    "moderate rain at times" -> R.drawable.rain
                    else -> R.drawable.clear
                }
                WeatherItem(data = result.data)

            }

            null -> {}
        }
    }

}

@Composable
fun WeatherItem(data: WeatherData) {

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "LOCAtion")
            Spacer(modifier = Modifier.padding(5.dp))


            Text(text = "${data.location.name}, ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            // Text(text = "${data.location.region}, ", fontSize = 20.sp)
            Text(text = "${data.location.country}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }


        Text(
            text = "${data.current.temp_c}°C",
            fontSize = 80.sp,
            style = MaterialTheme.typography.labelLarge
        )

        AsyncImage(
            modifier = Modifier.size(200.dp),

            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Image"
        )

        var condition by remember { mutableStateOf("") }
        condition = data.current.condition.text

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.2f) // Adjust alpha for transparency
            ),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = condition,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.DarkGray
                    )
                }
            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            //set shape of the card
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.3f) // Adjust alpha for transparency
            ),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        moreWeatherDetails("Wind Speed", "${data.current.wind_kph} km/h")
                        moreWeatherDetails("UV", "${data.current.uv}")
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        moreWeatherDetails("Feels like", "${data.current.feelslike_c}°C")
                        moreWeatherDetails("Humidity", "${data.current.humidity}%")
                    }
                }
            }
        )
    }


}

@Composable
fun moreWeatherDetails(key: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value, fontSize = 30.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            key,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
