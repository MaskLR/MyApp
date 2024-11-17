package com.lyy.myapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fax
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lyy.myapp.R
import com.lyy.myapp.ui.screens.DiscoverScreen
import com.lyy.myapp.ui.screens.HomeScreen
import com.lyy.myapp.ui.screens.MeScreen


// 定义导航路径常量
private const val HOME_ROUTE = "home"
private const val DISCOVER_ROUTE = "discover"
private const val ME_ROUTE = "me"
private const val DDNS_ROUTE = "ddns"

/**
 * 定义应用的主导航图
 * 使用 NavHost 来处理不同屏幕之间的导航
 * @param navController 导航控制器，管理导航操作
 * @param modifier 用于自定义组件的修饰符，默认为 Modifier
 */
// NavGraph 是一个 Composable 函数，定义应用的导航图。
// navController 用于在不同页面之间导航。
@Composable
fun NavGraph(navController: NavHostController) {
    // NavHost 组件是 Compose Navigation 中的核心，它定义了起始目的地和各个路由
    NavHost(navController = navController, startDestination = NavDestination.Home.route) {
        // 定义主页的路由和其内容
        composable(NavDestination.Home.route) { HomeScreen() }

        // 定义搜索页面的路由和其内容
        composable(NavDestination.Discover.route) { DiscoverScreen() }

        // 定义设置页面的路由和其内容
        composable(NavDestination.Me.route) { MeScreen() }
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
            label = { Text(text = stringResource(id = R.string.home)) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
            selected = navController.currentDestination?.route == HOME_ROUTE,
            onClick = { navController.navigate(HOME_ROUTE) }
        )
        NavigationBarItem(
            label = { Text(text = stringResource(id = R.string.home)) },
            icon = { Icon(Icons.Filled.Fax, contentDescription = "功能") },
            selected = navController.currentDestination?.route == DISCOVER_ROUTE,
            onClick = { navController.navigate(DISCOVER_ROUTE) }
        )
        NavigationBarItem(
            label = { Text(text = stringResource(id = R.string.me)) },
            icon = { Icon(Icons.Filled.PersonAdd, contentDescription = "我") },
            selected = navController.currentDestination?.route == ME_ROUTE,
            onClick = { navController.navigate(ME_ROUTE) }
        )
    }
}