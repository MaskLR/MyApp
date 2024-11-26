package com.lyy.myapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun HomeScreen() {
    Text("首页")
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
     HomeScreen()
}
