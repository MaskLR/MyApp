package com.lyy.myapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.lyy.myapp.navigation.NavGraph
import com.lyy.myapp.ui.theme.MyAppTheme
import com.lyy.myapp.ui.components.BottomNavBar


/**
 * MainActivity 是应用程序的入口点。
 * 负责设置 Compose 的内容视图。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //全屏显示
            enableEdgeToEdge()
            // 使用主题包装内容
            MyAppTheme {
                Surface {
                    // 启动主应用程序
                    MyApp()
                }
            }
        }
    }
}

/**
 * MyApp 组件设置应用的整体布局。
 * 使用 Scaffold 布局组件来添加底部导航栏。
 */
// MyApp 是整个应用的根 Composable，包含底部导航栏和导航图。
@Composable
fun MyApp() {
    // 创建一个 NavController 来管理导航
    val navController = rememberNavController()

    // Scaffold 是 Compose 中的一个布局组件，用于创建标准的应用结构
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) } // 设置底部导航栏
    ) { innerPadding ->
        // 使用 Box 作为根布局，并设置内边距，以防止导航栏遮挡内容
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController) // 设置导航图，负责管理各页面之间的切换
        }
    }
}

    /**
     * DefaultPreview 用于在 Android Studio 中预览 MyApp 组件。
     */
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyAppTheme {
            // 显示 MyApp 组件的预览
            MyApp()
        }
    }
