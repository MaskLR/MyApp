package com.lyy.myapp.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.NetworkInterface

object IPv6Addresses {
    private const val IPV6_API_URL = "https://6.ipw.cn/"

    // 创建 Retrofit 实例
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(IPV6_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 创建接口服务
    private val apiService: IPv6ApiService by lazy {
        retrofit.create(IPv6ApiService::class.java)
    }

    suspend fun getIPv6Addresses(): List<String> = withContext(Dispatchers.IO) {
        val ipv6Addresses = mutableListOf<String>()

        try {
            // 获取本地网络接口的 IPv6 地址
            NetworkInterface.getNetworkInterfaces().toList().forEach { networkInterface ->
                if (networkInterface.name == "wlan0") {
                    ipv6Addresses.addAll(
                        networkInterface.inetAddresses.toList()
                            .filter { it is InetAddress && it.address.size == 16 }
                            .mapNotNull { it.hostAddress?.takeIf { addr -> !addr.startsWith("fe80:") } }
                    )
                }
            }

            // 如果没有找到本地的 IPv6 地址，尝试从 API 获取
            if (ipv6Addresses.isEmpty()) {
                fetchIPv6FromApi()?.let { ipv6Addresses.add(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        ipv6Addresses
    }

    // 使用 Retrofit 获取 API 提供的 IPv6 地址
    private suspend fun fetchIPv6FromApi(): String? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getIPv6()

            // 处理 API 返回的响应
            if (response.isNotBlank()) {
                response.trim()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
