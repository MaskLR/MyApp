package com.lyy.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lyy.myapp.ui.screens.HomeScreen
import com.lyy.myapp.ui.screens.LoginScreen
import com.lyy.myapp.viewmodel.LoginViewModel

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel) }
        composable("home") { HomeScreen() }
        // 添加注册页面逻辑...
    }
}

