package com.lyy.myapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyy.myapp.ui.theme.MyAppTheme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import com.lyy.myapp.network.NetworkClient
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                // 设置应用的主界面
                MyAppScreen()
            }
        }
    }

    @Composable
    fun MyAppScreen() {
        // 控制登录对话框的显示状态
        var showLoginDialog by remember { mutableStateOf(true) }
        // 保存用户名
        var username by remember { mutableStateOf("") }

        // 根据状态显示登录对话框或问候语
        if (showLoginDialog) {
            LoginDialog(onLoginSuccess = { user ->
                username = user
                showLoginDialog = false
            })
        } else {
            Greeting(username)
        }
    }

    @Composable
    fun LoginDialog(onLoginSuccess: (String) -> Unit) {
        // 登录对话框中的状态
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { /* 不处理对话框外点击事件 */ },
            title = { Text("登录") },
            text = {
                Column {
                    // 账号输入字段
                    InputField(value = username, onValueChange = { username = it }, label = "账号")
                    Spacer(modifier = Modifier.height(8.dp))
                    // 密码输入字段
                    InputField(
                        value = password,
                        onValueChange = { password = it },
                        label = "密码",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    // 显示错误信息
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    isLoading = true
                    // 模拟网络登录操作
                    NetworkClient.login(username, password) { result ->
                        isLoading = false
                        result.onSuccess { user ->
                            onLoginSuccess(user)
                        }.onFailure { error ->
                            errorMessage = error.message
                        }
                    }
                }) {
                    // 根据加载状态显示不同内容
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("登录")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { finish() }) {
                    Text("取消")
                }
            }
        )
    }

    @Composable
    fun InputField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        visualTransformation: VisualTransformation = VisualTransformation.None
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            visualTransformation = visualTransformation
        )
    }

    @Composable
    fun Greeting(name: String) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(vertical = 16.dp) // 垂直居中文本
            ) {
                Text(
                    text = "My APP @Mask-lyy",
                    color = Color.White, // 在背景色上使用白色文本
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = "你好$name!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyAppTheme {
            Greeting("Android")
        }
    }
}
