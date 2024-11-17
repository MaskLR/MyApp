package com.lyy.myapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.lyy.myapp.R


// NavDestination 是一个密封类，表示底部导航栏中的页面配置。
// 每个页面都有一个路由名称、图标和标签。
sealed class NavDestination(
    val route: String,
    val label: Int,
    val icon: ImageVector,
    val iconDescription: Int
) {
    // 主页目的地，使用home路由，图标为Home，标签为“Home”
    object Home : NavDestination("home", R.string.home, Icons.Filled.Home, R.string.home)

    // 搜索页面目的地，使用discover路由，图标为Search，标签为“Search”
    object Discover :
        NavDestination("discover", R.string.Discover, Icons.Filled.Api, R.string.Discover)


    // 设置页面目的地，使用me路由，图标为Settings，标签为“Settings”
    object Me : NavDestination("me", R.string.me, Icons.Filled.Person, R.string.me)
}

