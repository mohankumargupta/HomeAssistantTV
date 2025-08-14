package com.mohankumargupta.homeassistanttv.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mohankumargupta.homeassistanttv.data.local.PreferencesDataSourceImpl
import com.mohankumargupta.homeassistanttv.data.local.PreferencesDataStore
import com.mohankumargupta.homeassistanttv.data.remote.HomeAssistantWebSocketDataSourceImpl
import com.mohankumargupta.homeassistanttv.data.remote.MDNSDataSource
import com.mohankumargupta.homeassistanttv.data.remote.MDNSDataSourceImpl
import com.mohankumargupta.homeassistanttv.data.remote.WebSocketDataSource
import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepository
import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepositoryImpl
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HaConnectionDataStore

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindHomeAssistantRepository(
        impl: HomeAssistantRepositoryImpl
    ): HomeAssistantRepository

    @Binds
    @Singleton // Ensure this matches the scope on MDNSRepositoryImpl if any
    abstract fun bindMDNSRepository(
        mdnsRepositoryImpl: MDNSRepositoryImpl
    ): MDNSRepository

    @Binds
    @Singleton // Match the scope of the implementation if any
    abstract fun bindMDNSDataSource(
        impl: MDNSDataSourceImpl
    ): MDNSDataSource

    @Binds
    @Singleton
    abstract fun bindWebSocketDataSource(
        impl: HomeAssistantWebSocketDataSourceImpl
    ): WebSocketDataSource

    @Binds
    @Singleton
    abstract fun bindPreferencesDataSource(
        impl: PreferencesDataSourceImpl
    ): PreferencesDataStore

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().build()

        @Provides
        @Singleton
        @HaConnectionDataStore
        fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<androidx.datastore.preferences.core.Preferences> =
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("ha_connection") }
            )
    }
}
