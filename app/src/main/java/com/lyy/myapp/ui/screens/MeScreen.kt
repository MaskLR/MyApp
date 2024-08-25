package com.lyy.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyy.myapp.network.NetworkClient
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

/**
 * 主界面：显示登录或注册界面
 */
@Composable
fun MeScreen() {
    val context = LocalContext.current // 获取当前上下文
    var isLoginMode by remember { mutableStateOf(true) } // 控制显示登录或注册模式
    val snackbarHostState = remember { SnackbarHostState() } // 用于显示 Snackbar

    // 初始化 NetworkClient，使用提供的上下文
    LaunchedEffect(context) {
        NetworkClient.init(context)
    }

    // 根据当前模式显示相应的界面
    if (isLoginMode) {
        LoginScreen(
            context = context,
            onSwitchMode = { isLoginMode = false },
            snackbarHostState = snackbarHostState
        )
    } else {
        RegisterScreen(
            context = context,
            onSwitchMode = { isLoginMode = true },
            snackbarHostState = snackbarHostState
        )
    }

    // 显示 Snackbar
    SnackbarHost(hostState = snackbarHostState)
}

/**
 * 登录界面
 * @param context 上下文，用于执行网络请求
 * @param onSwitchMode 切换到注册界面的回调函数
 * @param snackbarHostState 用于显示 Snackbar
 */
@Composable
fun LoginScreen(
    context: Context,
    onSwitchMode: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var nickname by remember { mutableStateOf<String?>(null) } // 用于存储获取到的 nickname
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "登录", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                NetworkClient.login(username, password) { success, responseMessage, userNickname ->
                    // 更新状态以反映登录结果
                    if (success) {
                        // 设置获取到的 nickname
                        nickname = userNickname
                        // 显示登录成功消息和 nickname
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("登录成功，欢迎 $nickname")
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(responseMessage ?: "")
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "登录")
        }

        Spacer(modifier = Modifier.height(16.dp))

        message?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSwitchMode) {
            Text(text = "切换到注册")
        }
    }

    SnackbarHost(hostState = snackbarHostState) // 确保 SnackbarHost 在屏幕上显示
}


/**
 * 注册界面
 * @param context 上下文，用于执行网络请求
 * @param onSwitchMode 切换到登录界面的回调函数
 * @param snackbarHostState 用于显示 Snackbar
 */
@Composable
fun RegisterScreen(context: Context, onSwitchMode: () -> Unit, snackbarHostState: SnackbarHostState) {
    var nickname by remember { mutableStateOf("") } // 昵称输入
    var username by remember { mutableStateOf("") } // 用户名输入
    var password by remember { mutableStateOf("") } // 密码输入
    var message by remember { mutableStateOf<String?>(null) } // 显示注册结果信息
    var isLoading by remember { mutableStateOf(false) } // 注册按钮的加载状态
    val coroutineScope = rememberCoroutineScope() // 用于启动协程

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "注册", style = MaterialTheme.typography.titleLarge) // 注册标题

        Spacer(modifier = Modifier.height(8.dp)) // 间隔

        // 昵称输入框
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("昵称") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp)) // 间隔

        // 用户名输入框
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp)) // 间隔

        // 密码输入框
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(), // 密码隐藏显示
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp)) // 间隔

        // 注册按钮
        Button(
            onClick = {
                // 检查输入是否完整
                if (username.isBlank() || password.isBlank() || nickname.isBlank()) {
                    message = "所有字段都必须填写。"
                    return@Button
                }

                isLoading = true // 开始加载

                // 调用 NetworkClient 的注册方法
                NetworkClient.register(username, password, nickname) { success, responseMessage ->
                    isLoading = false // 加载结束
                    message = responseMessage // 设置注册结果信息
                    if (success) {
                        // 注册成功后的处理逻辑（如导航到登录页面）
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("注册成功，请登录")
                            // 切换到登录页面
                            onSwitchMode()
                        }
                    } else {
                        // 显示注册失败消息
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(responseMessage ?: "")
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLoading) "正在注册..." else "注册")
        }

        Spacer(modifier = Modifier.height(16.dp)) // 间隔

        // 显示注册结果信息
        message?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp)) // 间隔

        // 切换到登录界面的按钮
        TextButton(onClick = onSwitchMode) {
            Text(text = "切换到登录")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMeScreen() {
    MeScreen()
}
