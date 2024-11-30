package com.lyy.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lyy.myapp.navigation.NavGraph
import com.lyy.myapp.ui.theme.MyAppTheme
import com.lyy.myapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()            //全屏显示
            MyAppTheme {
                val navController = rememberNavController()
                val loginViewModel = hiltViewModel<LoginViewModel>()
                NavGraph(navController, loginViewModel)
            }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAppTheme {
        // 显示 MyApp 组件的预览
    }
}
