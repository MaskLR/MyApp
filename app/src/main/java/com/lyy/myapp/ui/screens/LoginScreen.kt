package com.lyy.myapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lyy.myapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    // 获取登录状态流的最新状态
    val loginState by viewModel.loginState.collectAsState()

    // 定义用户名和密码输入框的状态
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 获取当前上下文（用于显示 Toast）
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 用户名输入框
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("用户名") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 密码输入框
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                visualTransformation = PasswordVisualTransformation(), // 密码字段，使用密码视觉转换
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 登录按钮
            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登录")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 注册按钮
            TextButton(onClick = { navController.navigate("register") }) {
                Text("注册")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 跳过登录，直接进入主页
            TextButton(onClick = { navController.navigate("home") }) {
                Text("跳过登录")
            }
        }
    }

    // 根据登录状态进行相应的 UI 更新
    when (val state = loginState) {
        is LoginViewModel.LoginState.Loading -> {
            // 显示加载进度条
            CircularProgressIndicator()
        }
        is LoginViewModel.LoginState.Success -> {
            // 登录成功，显示欢迎弹窗并跳转到主页
            LaunchedEffect(Unit) {
                Toast.makeText(context, "欢迎 ${state.nickname}", Toast.LENGTH_LONG).show()
                navController.navigate("home") { popUpTo("login") { inclusive = true } }
            }
        }
        is LoginViewModel.LoginState.Error -> {
            // 登录失败，显示错误信息
            LaunchedEffect(Unit) {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }
}
