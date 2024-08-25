package com.lyy.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.lyy.myapp.ui.navigation.MyAppNavHost
import com.lyy.myapp.ui.navigation.BottomNavigationBar
import com.lyy.myapp.ui.theme.MyAppTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext

/**
 * MainActivity 是应用程序的入口点。
 * 负责设置 Compose 的内容视图。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 使用主题包装内容
            MyAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
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
@Composable
fun MyApp() {
    // 创建 NavController 实例来处理应用导航
    val navController = rememberNavController()

    // 使用 Scaffold 布局来包含底部导航栏
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // 添加底部导航栏
    ) { innerPadding ->
        // 将导航主机（NavHost）放置在 Scaffold 的主体部分，并传递内边距
        MyAppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding) // 确保内容不被底部导航栏遮挡
        )
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
