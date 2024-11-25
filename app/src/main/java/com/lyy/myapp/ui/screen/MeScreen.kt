package com.lyy.myapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyy.myapp.R
import com.lyy.myapp.viewmodel.MeViewModel
import kotlinx.coroutines.launch


@Composable
fun MeScreen(viewModel: MeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // 收集 ViewModel 的状态，状态会触发 UI 更新
    val uiState by viewModel.uiState.collectAsState()

    // 用于显示 SnackBar 提示信息的状态对象
    val snackbarHostState = remember { SnackbarHostState() }

    // 创建协程作用域，用于在 UI 状态更新时执行异步操作
    val coroutineScope = rememberCoroutineScope()

    // 监听 UI 状态的 message 字段，若有新消息则显示在 SnackBar 中
    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    // 显示登录或注册界面
    AuthScreen(
        title = if (uiState.isLoginMode) stringResource(id = R.string.login) else stringResource(id = R.string.register),
        primaryActionText = if (uiState.isLoginMode) stringResource(id = R.string.login) else stringResource(id = R.string.register),
        secondaryActionText = if (uiState.isLoginMode) stringResource(id = R.string.switch_to_register) else stringResource(id = R.string.switch_to_login),
        username = uiState.username,
        onUsernameChange = viewModel::onUsernameChange,
        password = uiState.password,
        onPasswordChange = viewModel::onPasswordChange,
        nickname = uiState.nickname.takeIf { !uiState.isLoginMode }, // 注册模式显示昵称
        onNicknameChange = viewModel::onNicknameChange,
        isLoading = uiState.isLoading,
        onPrimaryAction = viewModel::performPrimaryAction,
        onSwitchMode = viewModel::switchMode,
        snackbarHostState = snackbarHostState
    )
}

/**
 * AuthScreen - 登录或注册界面，包含输入框和操作按钮
 */
@Composable
fun AuthScreen(
    title: String,
    primaryActionText: String,
    secondaryActionText: String,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    nickname: String?,
    onNicknameChange: ((String) -> Unit)?,
    isLoading: Boolean,
    onPrimaryAction: () -> Unit,
    onSwitchMode: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // 标题，显示当前是登录还是注册模式
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // 用户名输入框
        AuthTextField(value = username, onValueChange = onUsernameChange, label = stringResource(id = R.string.Username))
        Spacer(modifier = Modifier.height(8.dp))

        // 密码输入框
        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(id = R.string.password),
            isPassword = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 昵称输入框（仅在注册模式下显示）
        nickname?.let {
            AuthTextField(value = it, onValueChange = onNicknameChange!!, label = stringResource(id = R.string.nickname))
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 主操作按钮：登录或注册
        Button(
            onClick = onPrimaryAction,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 显示“正在” + 操作文本，或直接显示操作文本
            Text(text = if (isLoading) stringResource(id = R.string.loading, primaryActionText) else primaryActionText)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 切换模式按钮：从登录切换到注册，或从注册切换到登录
        TextButton(onClick = onSwitchMode) {
            Text(text = secondaryActionText)
        }
    }

    // SnackBar 显示区域
    SnackbarHost(hostState = snackbarHostState)
}

/**
 * AuthTextField - 自定义的输入框组件，用于用户名、密码和昵称输入
 */
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 预览界面，用于查看界面效果
 */
@Preview(showBackground = true)
@Composable
fun PreviewMeScreen() {
    MeScreen()
}
