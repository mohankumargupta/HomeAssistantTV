package com.mohankumargupta.homeassistanttv.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mohankumargupta.homeassistanttv.data.model.UserPreferences
import com.mohankumargupta.homeassistanttv.di.HaConnectionDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
    @param: HaConnectionDataStore private val dataStore: DataStore<Preferences>
) : PreferencesDataStore {

    private object PreferencesKeys {
        val HOME_ASSISTANT_IP = stringPreferencesKey("home_assistant_ip")
        val HOME_ASSISTANT_PORT = intPreferencesKey("home_assistant_port")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    override fun getPreferences() = dataStore.data.map { preferences ->
        UserPreferences(
            homeAssistantIp = preferences[PreferencesKeys.HOME_ASSISTANT_IP] ?: "",
            homeAssistantPort = preferences[PreferencesKeys.HOME_ASSISTANT_PORT] ?: 8123,
            accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: ""
        )
    }

    override suspend fun savePreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HOME_ASSISTANT_IP] = userPreferences.homeAssistantIp
            preferences[PreferencesKeys.HOME_ASSISTANT_PORT] = userPreferences.homeAssistantPort
            preferences[PreferencesKeys.ACCESS_TOKEN] = userPreferences.accessToken
        }
    }
}
