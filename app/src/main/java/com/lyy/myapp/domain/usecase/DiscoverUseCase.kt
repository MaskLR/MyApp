package com.lyy.myapp.domain.usecase


import com.lyy.myapp.data.network.IPv6Addresses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiscoverUseCase {
    suspend fun getIPv6Addresses(): List<String> {
        return withContext(Dispatchers.IO) {
            IPv6Addresses.getIPv6Addresses()
        }
    }
}
