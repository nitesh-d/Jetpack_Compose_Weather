package com.example.myweather.ui.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.api.Constant
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.RetrofitInstance
import com.example.myweather.api.WeatherData
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val weatherData = MutableLiveData<NetworkResponse<WeatherData>>()
  val weatherResult: LiveData<NetworkResponse<WeatherData>> = weatherData
    fun getData(city: String) {

        weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {

                        weatherData.value = NetworkResponse.Success(it)
                    }
                } else {
                    weatherData.value = NetworkResponse.Error("Failed to load data")
                }

            } catch (e: Exception) {
                weatherData.value = NetworkResponse.Error("Failed to load data")
            }
        }
    }}