package com.lyy.myapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fax
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lyy.myapp.ui.screens.HomeScreen
import com.lyy.myapp.ui.screens.Function
import com.lyy.myapp.ui.screens.MeScreen
import com.lyy.myapp.ui.screens.function.DDNSPage

// 定义导航路径常量
private const val HOME_ROUTE = "home"
private const val FUNCTION_ROUTE = "function"
private const val ME_ROUTE = "me"
private const val DDNS_ROUTE = "ddns"

/**
 * 定义应用的主导航图
 * 使用 NavHost 来处理不同屏幕之间的导航
 * @param navController 导航控制器，管理导航操作
 * @param modifier 用于自定义组件的修饰符，默认为 Modifier
 */
@Composable
fun MyAppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE, // 指定启动屏幕为主页
        modifier = modifier // 应用传入的修饰符
    ) {
        composable(HOME_ROUTE) { HomeScreen() } // 主页
        composable(FUNCTION_ROUTE) { Function(navController) } // 功能页面
        composable(ME_ROUTE) { MeScreen() } // 个人页面
        composable(DDNS_ROUTE) { DDNSPage() } // DDNS 页面
    }
}

/**
 * 定义底部导航栏组件
 * @param navController 导航控制器，管理导航操作
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            label = { Text("首页") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
            selected = navController.currentDestination?.route == HOME_ROUTE,
            onClick = { navController.navigate(HOME_ROUTE) }
        )
        NavigationBarItem(
            label = { Text("功能") },
            icon = { Icon(Icons.Filled.Fax, contentDescription = "功能") },
            selected = navController.currentDestination?.route == FUNCTION_ROUTE,
            onClick = { navController.navigate(FUNCTION_ROUTE) }
        )
        NavigationBarItem(
            label = { Text("我") },
            icon = { Icon(Icons.Filled.PersonAdd, contentDescription = "我") },
            selected = navController.currentDestination?.route == ME_ROUTE,
            onClick = { navController.navigate(ME_ROUTE) }
        )
    }
}