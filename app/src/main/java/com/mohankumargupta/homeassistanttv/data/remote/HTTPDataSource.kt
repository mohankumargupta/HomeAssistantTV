package com.mohankumargupta.homeassistanttv.data.remote

import retrofit2.http.GET

interface HTTPDataSource {
    @GET("endpoint/home-assistant-TV")
    suspend fun getAccessToken(): String
}
