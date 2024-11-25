package com.lyy.myapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lyy.myapp.navigation.AppNavigation
import com.lyy.myapp.ui.theme.MyAppTheme


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
                    AppNavigation()
                }
            }
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
        }
    }
