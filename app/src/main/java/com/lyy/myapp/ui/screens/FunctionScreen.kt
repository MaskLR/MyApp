package com.lyy.myapp.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Function(navController: NavController) {
    // 获取设备型号
    val deviceModel = Build.MODEL

    // 使用 Column 布局，将内容垂直排列
    Column(modifier = Modifier.padding(16.dp)) {

        // 显示设备型号的 Text 组件
        Text(
            text = "设备型号: $deviceModel",
            style = MaterialTheme.typography.bodyLarge
        )

        // 在设备型号和按钮之间添加一个垂直间距
        Spacer(modifier = Modifier.height(16.dp))

        // 显示一个按钮，点击后导航到 DDNS 页面
        Button(onClick = {
            // 使用 navController 导航到 "ddns" 路由
            navController.navigate("ddns")
        }) {
            // 按钮上的文本
            Text(text = "前往 DDNS")
        }
    }
}
