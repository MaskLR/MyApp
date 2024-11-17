package com.lyy.myapp.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.TimeUnit

object IPv6Addresses {
    //打印日志
    // private const val TAG = "IPv6Addresses"
    private const val IPV6_API_URL = "https://6.ipw.cn/"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    suspend fun getIPv6Addresses(): List<String> = withContext(Dispatchers.IO) {
        val ipv6Addresses = mutableListOf<String>()

        try {
            NetworkInterface.getNetworkInterfaces().toList().forEach { networkInterface ->
                if (networkInterface.name == "wlan0") {
                    ipv6Addresses.addAll(
                        networkInterface.inetAddresses.toList()
                            .filter { it is InetAddress && it.address.size == 16 }
                            .mapNotNull { it.hostAddress?.takeIf { addr -> !addr.startsWith("fe80:") } }
                    )
                }
            }

            if (ipv6Addresses.isEmpty()) {
                fetchIPv6FromApi()?.let { ipv6Addresses.add(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        ipv6Addresses
    }

    private suspend fun fetchIPv6FromApi(): String? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(IPV6_API_URL)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()?.trim()
                } else {
                    null
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
