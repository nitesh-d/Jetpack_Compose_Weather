package com.example.myweather.domain

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "weather_preferences")
class PreferenceManager(context: Context) {

    private val CITY_KEY = stringPreferencesKey("selected_city")

    // DataStore instance
    private val dataStore = context.dataStore

    // Flow to observe the city value
    val selectedCity: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[CITY_KEY] // Default is null if no city is set
        }

    // Save city value
    suspend fun setCity(city: String) {
        dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
            Log.d("Nick","Saved City")
        }
    }
}