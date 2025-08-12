package com.mohankumargupta.homeassistanttv.data.repository

import com.mohankumargupta.homeassistanttv.data.model.Endpoint
import kotlinx.coroutines.flow.Flow

interface MDNSRepository {
    fun discoverEndpoints(service: String): Flow<List<Endpoint>>
    fun getAccessToken(endpoint: Endpoint): Flow<String>
    //fun resolveService(endpoint: Endpoint): Flow<Endpoint>
}
