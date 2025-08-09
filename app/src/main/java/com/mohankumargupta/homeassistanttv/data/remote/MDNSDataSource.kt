package com.mohankumargupta.homeassistanttv.data.remote

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface MDNSDataSource {
    fun discoverServices(context: Context, service: String) : Flow<DiscoveryEvent>
}
