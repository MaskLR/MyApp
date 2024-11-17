package com.lyy.myapp.ui.components

// BottomNavBar.kt

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lyy.myapp.navigation.NavDestination

@Composable
fun BottomNavBar(navController: NavController) {
    // 定义导航栏中的页面列表，包含三个页面：Home、Discover 和 Me
    val items = listOf(NavDestination.Home, NavDestination.Discover, NavDestination.Me)

    // 使用 NavigationBar 来替代传统的 BottomNavigation（这是 Material 3 中的新组件）
    // containerColor 设置底部导航栏的背景颜色，使用 MaterialTheme 提供的颜色方案中的 primary 颜色
    // contentColor 设置图标和文字的颜色，这里使用白色
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary, // 设置底部导航栏背景色为主题的主色
        contentColor = MaterialTheme.colorScheme.primary // 设置导航项的图标和文字颜色为白色
    ) {
        // 获取当前的路由（即当前显示的页面）
        // navController.currentBackStackEntryAsState() 获取当前页面的 BackStackEntry 状态
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        // 从当前的路由中提取出路由信息，作为判断是否选中该导航项的依据
        val currentRoute = navBackStackEntry.value?.destination?.route

        // 遍历所有的页面项，并为每一项创建一个 NavigationBarItem
        items.forEach { destination ->
            // 创建一个 NavigationBarItem
            // icon 为当前页面的图标
            // label 为当前页面的文本标签
            // selected 用来判断当前导航项是否被选中，如果当前路由等于目标路由，则选中该项
            // onClick 事件处理：如果当前路由和目标路由不同，执行导航操作
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(id = destination.iconDescription)
                    )
                }, // 设置导航项的图标
                label = { Text(text = stringResource(id = destination.label)) }, // 设置导航项的标签文字
                selected = currentRoute == destination.route, // 判断当前路由是否和目标路由一致，来决定是否选中该项
                onClick = {
                    // 只有在当前路由和目标路由不同的情况下，才执行导航操作
                    if (currentRoute != destination.route) {
                        navController.navigate(destination.route) {
                            // popUpTo 操作：回到导航图的起始点（避免堆积重复的页面）
                            // launchSingleTop = true：防止重复创建同一目标页面
                            popUpTo(navController.graph.startDestinationId) // 弹出到起始目标
                            launchSingleTop = true // 防止重复加载目标页面
                        }
                    }
                }
            )
        }
    }
}
