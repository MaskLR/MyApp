package com.lyy.myapp.network

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.InetAddress
import java.net.NetworkInterface

object IPv6Addresses {

    private const val TAG = "NetworkUtils"
    private const val IPV6_API_URL = "https://6.ipw.cn/"

    // 初始化 OkHttpClient 实例，用于进行网络请求
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    /**
     * 获取设备的 IPv6 地址。如果设备上无法获取 IPv6 地址，则从 API 获取。
     * @param context 上下文对象，用于访问系统服务。
     * @return 返回一个包含所有找到的 IPv6 地址的列表。如果没有找到 IPv6 地址，则从 API 获取的 IPv6 地址。
     */
    suspend fun getIPv6Addresses(context: Context): List<String> {
        val ipv6Addresses = mutableListOf<String>()

        try {
            // 尝试获取所有网络接口
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in interfaces) {
                Log.d(TAG, "接口名称: ${networkInterface.name}")

                // 只处理 Wi-Fi 网络接口
                if (networkInterface.name == "wlan0") {
                    for (address in networkInterface.inetAddresses) {
                        // 检查地址是否为 IPv6 地址（16 字节长度）
                        if (address is InetAddress && address.address.size == 16) {
                            val ipv6Address = address.hostAddress
                            // 过滤掉以 "fe80:" 开头的链路本地地址
                            if (!ipv6Address.startsWith("fe80:")) {
                                ipv6Addresses.add(ipv6Address)
                            }
                        }
                    }
                }
            }

            // 如果找到 IPv6 地址，则直接返回
            if (ipv6Addresses.isNotEmpty()) {
                return ipv6Addresses
            }

            // 如果未找到 IPv6 地址，则从 API 获取 IPv6 地址
            val ipv6Address = fetchIPv6FromApi()
            if (ipv6Address != null) {
                Log.d(TAG, "从 API 获取的 IPv6 地址: $ipv6Address")
                // 这里假设从 API 获取的地址是有效的IPv6地址，不需要进一步过滤
                ipv6Addresses.add(ipv6Address)
            }

        } catch (e: Exception) {
            Log.e(TAG, "获取 IPv6 地址时出错", e)
        }

        return ipv6Addresses
    }

    /**
     * 从指定的 API 获取 IPv6 地址。
     * @return 返回从 API 获取的 IPv6 地址。如果请求失败或出错，返回 null。
     */
    private suspend fun fetchIPv6FromApi(): String? {
        return withContext(Dispatchers.IO) {
            var ipv6Address: String? = null
            try {
                // 构建请求对象
                val request = Request.Builder()
                    .url(IPV6_API_URL)
                    .build()

                // 执行请求
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    // 处理响应内容
                    val responseData = response.body?.string()
                    Log.d(TAG, "API 响应内容: $responseData")
                    ipv6Address = responseData?.trim() // 去除多余的空格
                } else {
                    Log.e(TAG, "API 请求失败，状态码: ${response.code}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "从 API 获取 IPv6 地址时出错", e)
            }
            ipv6Address
        }
    }
}
