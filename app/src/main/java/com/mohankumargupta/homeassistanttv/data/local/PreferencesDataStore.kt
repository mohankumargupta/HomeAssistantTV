package com.mohankumargupta.homeassistanttv.data.local

import com.mohankumargupta.homeassistanttv.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    fun getPreferences(): Flow<UserPreferences>
    suspend fun savePreferences(userPreferences: UserPreferences)
}