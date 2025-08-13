package com.mohankumargupta.homeassistanttv.di

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
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

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

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().build()
    }
}