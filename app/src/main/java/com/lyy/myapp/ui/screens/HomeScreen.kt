package com.lyy.myapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyy.myapp.viewmodel.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    // 使用 homeViewModel 来获取数据并更新 UI
    val state = homeViewModel.state.collectAsState() // 收集状态
    Text(text = state.value) // 使用状态值更新 UI
    Text("首页")
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
