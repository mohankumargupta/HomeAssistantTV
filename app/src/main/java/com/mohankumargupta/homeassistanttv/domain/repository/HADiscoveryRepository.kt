package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.data.model.HAInstance
import kotlinx.coroutines.flow.Flow

interface HADiscoveryRepository {
    fun discoverEndpoints(): Flow<List<HAInstance>>
}