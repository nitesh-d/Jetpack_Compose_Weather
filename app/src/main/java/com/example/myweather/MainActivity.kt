package com.example.myweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.domain.PreferenceManager
import com.example.myweather.ui.theme.MyWeatherTheme
import com.example.myweather.ui.theme.WeatherPage
import com.example.myweather.ui.theme.WeatherViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceManager = PreferenceManager(applicationContext)
        val weatherViewModel = WeatherViewModel(preferenceManager)


        //val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        //enableEdgeToEdge()

        setContent {
            MyWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {

                    WeatherPage(weatherViewModel)


                }
            }

        }
    }
}

