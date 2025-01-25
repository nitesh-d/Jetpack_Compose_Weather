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
import com.example.myweather.domain.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WeatherViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val weatherData = MutableLiveData<NetworkResponse<WeatherData>>()
    val weatherResult: LiveData<NetworkResponse<WeatherData>> = weatherData
    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity

    init {
        // Initialize with the saved city from DataStore
        viewModelScope.launch {

            _selectedCity.value = preferenceManager.selectedCity.first()

          getData(_selectedCity.value.toString())
            Log.d("Nick", _selectedCity.value.toString())
        }
    }

    fun updateCity(city: String) {
        viewModelScope.launch {
            preferenceManager.setCity(city)
            _selectedCity.value = city
           getData(city)
            Log.d("Nick", "City updated: $city")

        }
    }
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
    }
}