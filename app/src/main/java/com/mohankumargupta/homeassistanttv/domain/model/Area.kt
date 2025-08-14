package com.mohankumargupta.homeassistanttv.domain.model

import kotlinx.serialization.SerialName

data class Area(
    @SerialName("area_id")
    val areaId: String,
    val name: String,
    @SerialName("picture")
    val pictureUrl: String? = null
)