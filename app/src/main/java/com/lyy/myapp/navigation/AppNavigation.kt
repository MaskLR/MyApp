package com.lyy.myapp.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lyy.myapp.R
import com.lyy.myapp.ui.screens.DiscoverScreen
import com.lyy.myapp.ui.screens.HomeScreen
import com.lyy.myapp.ui.screens.MeScreen

/**
 * 定义应用内导航的各个路由。
 * 每个路由包含路径(route)、标签(label，支持国际化)以及图标(icon)。
 */
sealed class Route(val route: String, @StringRes val label: Int, val icon: ImageVector) {
    object Home : Route("home", R.string.home, Icons.Filled.Home)
    object Discover : Route("discover", R.string.discover, Icons.Filled.Api)
    object Me : Route("me", R.string.me, Icons.Filled.Person)

    // companion object 用于统一管理路由列表
    companion object {
        val items = listOf(Home, Discover, Me)
    }
}

@Composable
fun AppNavigation() {
    // 创建 NavController，用于控制导航
    val navController = rememberNavController()

    Scaffold(
        // 设置全屏布局
        modifier = Modifier.fillMaxSize(),
        // 设置底部导航栏
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        // 配置 NavHost，管理各个路由的内容
        NavHost(
            navController = navController,
            startDestination = Route.Home.route, // 默认起始路由
            modifier = Modifier.padding(innerPadding) // 避免内容被底部导航栏遮挡
        ) {
            composable(Route.Home.route) { HomeScreen() } // 首页
            composable(Route.Discover.route) { DiscoverScreen() } // 发现页
            composable(Route.Me.route) { MeScreen() } // 我的页面
        }
    }
}

/**
 * 底部导航栏组件。
 *
 * @param navController 控制导航的 NavController
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // 获取当前路由，用于确定哪个导航项被选中
    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

    NavigationBar {
        // 遍历定义的路由列表，为每个路由生成一个导航项
        Route.items.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route.route, // 判断当前路由是否被选中
                onClick = {
                    if (currentRoute != route.route) {
                        // 点击导航项时进行导航，并配置相关参数
                        navController.navigate(route.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true } // 返回时保留状态
                            launchSingleTop = true // 避免多次导航到同一路由
                            restoreState = true // 恢复之前的状态
                        }
                    }
                },
                icon = { Icon(route.icon, contentDescription = null) }, // 图标
                label = { Text(stringResource(id = route.label)) } // 标签，使用国际化资源
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavigation() {
    AppNavigation()
    MeScreen()
}
