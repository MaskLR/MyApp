package com.lyy.myapp.ui.screens

import android.content.Context
// 用到的其他 Compose 和 Android 相关组件
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyy.myapp.data.network.NetworkClient
import kotlinx.coroutines.launch

// 用到的布局组件
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

// 用到的 Material3 组件
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField


// 用到的 Compose 状态管理和协程
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


/**
 * MeScreen - 主界面，用于显示登录或注册页面
 */
@Composable
fun MeScreen() {
    val context = LocalContext.current // 获取当前的上下文
    var isLoginMode by remember { mutableStateOf(true) } // 用于控制登录或注册模式的切换
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar的状态，用于显示提示信息

    // 初始化网络客户端（例如用来设置全局上下文或其他配置）
    LaunchedEffect(context) {
        NetworkClient.init(context)
    }

    // 根据 isLoginMode 的值，显示不同的界面
    if (isLoginMode) {
        AuthScreen(
            title = "登录", // 页面标题
            primaryActionText = "登录", // 主操作按钮文本
            secondaryActionText = "切换到注册", // 切换模式的按钮文本
            onPrimaryAction = { username, password, _, onResult ->
                // 调用 NetworkClient 的登录方法
                NetworkClient.login(username, password) { success, message, nickname ->
                    onResult(success, message ?: "", nickname) // 返回结果给 AuthScreen 进行后续处理
                }
            },
            onSwitchMode = { isLoginMode = false }, // 切换到注册模式
            snackbarHostState = snackbarHostState
        )
    } else {
        AuthScreen(
            title = "注册",
            primaryActionText = "注册",
            secondaryActionText = "切换到登录",
            onPrimaryAction = { username, password, nickname, onResult ->
                // 调用 NetworkClient 的注册方法
                NetworkClient.register(username, password, nickname ?: "") { success, message ->
                    onResult(success, message ?: "", null) // 返回注册结果给 AuthScreen
                }
            },
            onSwitchMode = { isLoginMode = true }, // 切换到登录模式
            snackbarHostState = snackbarHostState
        )
    }

    // 显示 Snackbar，用于提示消息（如登录成功、注册失败等）
    SnackbarHost(hostState = snackbarHostState)
}

/**
 * AuthScreen - 登录和注册的界面
 * @param title 当前操作的标题
 * @param primaryActionText 主操作按钮的文本
 * @param secondaryActionText 切换模式的按钮文本
 * @param onPrimaryAction 执行登录或注册的回调函数
 * @param onSwitchMode 切换模式的回调函数
 * @param snackbarHostState 显示 Snackbar 的状态
 */
@Composable
fun AuthScreen(
    title: String,
    primaryActionText: String,
    secondaryActionText: String,
    onPrimaryAction: (username: String, password: String, nickname: String?, onResult: (Boolean, String, String?) -> Unit) -> Unit,
    onSwitchMode: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var username by remember { mutableStateOf("") } // 用户名输入
    var password by remember { mutableStateOf("") } // 密码输入
    var nickname by remember { mutableStateOf("") } // 昵称输入（仅注册模式下使用）
    var message by remember { mutableStateOf<String?>(null) } // 错误或成功消息
    var isLoading by remember { mutableStateOf(false) } // 加载状态，用于禁用按钮
    val coroutineScope = rememberCoroutineScope() // 用于启动协程

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center // 垂直居中对齐
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge) // 显示标题

        Spacer(modifier = Modifier.height(8.dp)) // 添加空白间隔

        // 用户名输入框
        AuthTextField(value = username, onValueChange = { username = it }, label = "用户名")
        Spacer(modifier = Modifier.height(8.dp))

        // 密码输入框
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "密码",
            isPassword = true // 密码输入框需要隐藏字符
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 昵称输入框（仅在注册模式下显示）
        if (title == "注册") {
            AuthTextField(value = nickname, onValueChange = { nickname = it }, label = "昵称")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 主操作按钮（登录或注册）
        Button(
            onClick = {
                // 检查输入的完整性
                if (username.isBlank() || password.isBlank() || (title == "注册" && nickname.isBlank())) {
                    message = "所有字段都必须填写。"
                    return@Button
                }

                isLoading = true // 设置加载状态
                onPrimaryAction(username, password, if (title == "注册") nickname else null) { success, responseMessage, returnedNickname ->
                    isLoading = false // 关闭加载状态
                    message = responseMessage // 更新消息状态
                    coroutineScope.launch {
                        // 显示结果信息（成功时显示欢迎信息）
                        snackbarHostState.showSnackbar(
                            if (success) "$title 成功${returnedNickname?.let { "，欢迎 $it" } ?: ""}" else responseMessage
                        )
                        if (success && title == "注册") onSwitchMode() // 注册成功后切换到登录模式
                    }
                }
            },
            enabled = !isLoading, // 如果加载中则禁用按钮
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLoading) "正在${primaryActionText}..." else primaryActionText) // 根据加载状态显示文本
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示消息（错误或成功消息）
        message?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 切换模式的按钮
        TextButton(onClick = onSwitchMode) {
            Text(text = secondaryActionText)
        }
    }
}

/**
 * AuthTextField - 自定义输入框组件，用于显示用户名、密码或昵称的输入框
 * @param value 当前输入的文本
 * @param onValueChange 输入变化时的回调函数
 * @param label 输入框的标签文本
 * @param isPassword 如果为 true，则隐藏文本内容
 */
@Composable
fun AuthTextField(value: String, onValueChange: (String) -> Unit, label: String, isPassword: Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) }, // 显示输入框标签
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None, // 密码隐藏处理
        modifier = Modifier.fillMaxWidth() // 设置输入框宽度填满父布局
    )
}

/**
 * 预览 MeScreen 的界面
 */
@Preview(showBackground = true)
@Composable
fun PreviewMeScreen() {
    MeScreen()
}
