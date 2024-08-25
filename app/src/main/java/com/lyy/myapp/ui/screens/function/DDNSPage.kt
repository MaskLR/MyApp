package com.lyy.myapp.ui.screens.function

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.lyy.myapp.network.IPv6Addresses

@Composable
fun DDNSPage() {
    // 获取当前上下文
    val context = LocalContext.current

    // 状态变量用于存储 IPv6 地址列表
    var ipv6Addresses by remember { mutableStateOf<List<String>>(emptyList()) }
    // 状态变量用于显示是否正在加载
    var isLoading by remember { mutableStateOf(true) }
    // 状态变量用于显示错误信息
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 协程作用域，用于异步加载 IPv6 地址
    val scope = rememberCoroutineScope()

    // 启动协程来获取 IPv6 地址
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                // 切换到 IO 线程以执行网络操作
                val addresses = withContext(Dispatchers.IO) {
                    IPv6Addresses.getIPv6Addresses(context)
                }
                // 更新状态
                ipv6Addresses = addresses
            } catch (e: Exception) {
                // 如果发生错误，更新错误信息
                errorMessage = "获取 IPv6 地址时出错: ${e.message}"
            } finally {
                // 加载完成，无论成功还是失败，更新加载状态
                isLoading = false
            }
        }
    }

    // 组件的 UI 布局
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "DDNS Page",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This is the DDNS page.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 显示加载状态
        if (isLoading) {
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // 显示错误信息（如果有的话）
            errorMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // 显示获取到的 IPv6 地址
            if (ipv6Addresses.isNotEmpty()) {
                Text(
                    text = "IPv6 Addresses: ${ipv6Addresses.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "没有找到 IPv6 地址。",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
