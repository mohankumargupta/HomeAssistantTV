package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.data.model.Endpoint
import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepository
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeAssistantRepositoryImpl @Inject constructor(
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

    override suspend fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant) {
        val accessToken = mdnsRepository
            .getAccessToken(homeAssistant.toEndpoint())
            .catch { exception ->
                val j = 26
            }
            .collect { accessToken ->

            val i = 25
        }
    }

}

private fun HomeAssistant.toEndpoint(): Endpoint {
    return Endpoint(ip = ip, port = port)
}
