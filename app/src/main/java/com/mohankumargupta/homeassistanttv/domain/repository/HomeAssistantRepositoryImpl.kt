package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepository
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeAssistantRepositoryImpl(
    private val mdnsRepository: MDNSRepository
) : HomeAssistantRepository {
    val service = "_home-assistant._tcp."
    override fun discoverHomeAssistants(): Flow<List<HomeAssistant>> =
        mdnsRepository.discoverEndpoints(service).map { endpoints ->
            endpoints.map { endpoint ->
                val port = endpoint.port
                HomeAssistant(ip = endpoint.ip, port = port)
            }
        }
}
