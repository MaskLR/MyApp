package com.lyy.myapp.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyy.myapp.viewmodel.DiscoverViewModel

@Composable
fun DiscoverScreen() {
    // 获取 DiscoverViewModel 实例
    val discoverViewModel: DiscoverViewModel = viewModel()

    // 获取状态
    val state = discoverViewModel.state.collectAsState().value

    // 获取设备型号
    val deviceModel = Build.MODEL

    // LaunchedEffect 用来加载数据
    LaunchedEffect(Unit) {
        discoverViewModel.loadIPv6Addresses()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // 显示设备型号
        Text(text = "设备型号: $deviceModel")

        // 显示加载状态
        if (state.isLoading) {
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // 显示错误信息（如果有的话）
            state.errorMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // 显示获取到的 IPv6 地址
            if (state.ipv6Addresses.isNotEmpty()) {
                Text(
                    text = "IPv6地址:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(text = state.ipv6Addresses.joinToString("\n"))
            } else {
                Text(
                    text = "没有找到 IPv6 地址。",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
