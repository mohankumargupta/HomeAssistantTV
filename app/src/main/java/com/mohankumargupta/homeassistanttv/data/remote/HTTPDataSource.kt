package com.mohankumargupta.homeassistanttv.data.remote

import retrofit2.http.GET

interface HTTPDataSource {
    @GET("endpoint/home-assistant-tv")
    suspend fun getAccessToken(): String
}
