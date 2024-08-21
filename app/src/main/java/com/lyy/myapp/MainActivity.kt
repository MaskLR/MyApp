package com.lyy.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyy.myapp.ui.theme.MyAppTheme

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lyy.myapp.ui.theme.MyAppTheme
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : ComponentActivity() {

    private val client = OkHttpClient() // OkHttpClient 实例，用于发送网络请求

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 启用全屏模式
        setContent {
            MyAppTheme {
                // 设置应用主题和内容
                MyAppScreen()
            }
        }
    }

    @Composable
    fun MyAppScreen() {
        var showLoginDialog by remember { mutableStateOf(true) } // 控制是否显示登录对话框
        var username by remember { mutableStateOf("") } // 存储登录成功后的用户名

        if (showLoginDialog) {
            // 如果需要显示登录对话框
            LoginDialog(onLoginSuccess = { user ->
                username = user // 登录成功后保存用户名
                showLoginDialog = false // 隐藏登录对话框
            })
        } else {
            // 如果不需要显示登录对话框
            Greeting(username) // 显示欢迎信息
        }
    }

    @Composable
    fun LoginDialog(onLoginSuccess: (String) -> Unit) {
        var username by remember { mutableStateOf("") } // 存储输入的用户名
        var password by remember { mutableStateOf("") } // 存储输入的密码
        var isLoading by remember { mutableStateOf(false) } // 控制登录按钮的加载状态
        var errorMessage by remember { mutableStateOf<String?>(null) } // 存储错误信息

        AlertDialog(
            onDismissRequest = { /* Do nothing */ }, // 点击对话框外部区域时不做处理
            title = { Text("登录") }, // 对话框标题
            text = {
                Column {
                    TextField(
                        value = username,
                        onValueChange = { username = it }, // 更新用户名
                        label = { Text("账号") } // 用户名输入框标签
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // 输入框之间的间距
                    TextField(
                        value = password,
                        onValueChange = { password = it }, // 更新密码
                        label = { Text("密码") }, // 密码输入框标签
                        visualTransformation = PasswordVisualTransformation() // 隐藏密码输入的字符
                    )
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp)) // 错误信息和输入框之间的间距
                        Text(text = it, color = MaterialTheme.colorScheme.error) // 显示错误信息
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    isLoading = true // 按钮点击后进入加载状态
                    login(username, password, onLoginSuccess) { error ->
                        errorMessage = error // 设置错误信息
                        isLoading = false // 退出加载状态
                    }
                }) {
                    Text("登录")
                }
            },
            dismissButton = {
                TextButton(onClick = { /* Do nothing */ }) {
                    Text("取消") // 取消按钮
                }
            }
        )
    }

    private fun login(
        username: String,
        password: String,
        onLoginSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "http://mask.ddns.net:8888/api/loginUser.php" // 替换为你的后端登录接口 URL
        val json = JSONObject().apply {
            put("username", username) // 将用户名添加到 JSON 对象中
            put("password", password) // 将密码添加到 JSON 对象中
        }
        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType()) // 创建请求体

        val request = Request.Builder()
            .url(url) // 设置请求的 URL
            .post(requestBody) // 设置 POST 请求体
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 处理网络请求失败的情况
                runOnUiThread {
                    onError("Network error: ${e.message}") // 调用错误回调
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                // 登录成功
                                val nickname = jsonResponse.optString("nickname", "Unknown")
                                runOnUiThread {
                                    onLoginSuccess(nickname)
                                }
                            } else {
                                // 登录失败
                                val message = jsonResponse.optString("message", "Unknown error")
                                runOnUiThread {
                                    onError(message)
                                }
                            }
                        } catch (e: JSONException) {
                            // 解析 JSON 出错
                            runOnUiThread {
                                onError("Parsing error: ${e.message}")
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        onError("Server error: ${response.message}") // 调用错误回调
                    }
                }
            }
        })
    }

    @Composable
    fun Greeting(name: String) {
        Text(
            text = "Hello $name!",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyAppTheme {
            Greeting("Android")
        }
    }
}