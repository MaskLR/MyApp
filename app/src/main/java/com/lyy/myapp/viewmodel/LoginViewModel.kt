package com.lyy.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyy.myapp.domain.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Hilt 注解，自动注入依赖
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // 定义登录状态流，用于 UI 层观察并更新界面
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    // 登录方法
    fun login(username: String, password: String) {
        viewModelScope.launch {
            // 登录开始时进入加载状态
            _loginState.value = LoginState.Loading
            try {
                // 调用 UserRepository 进行登录操作
                val response = userRepository.login(username, password)
                // 登录成功，更新状态为成功并返回昵称
                _loginState.value = LoginState.Success(response.nickname)
            } catch (e: Exception) {
                // 登录失败，更新状态为错误并返回错误信息
                _loginState.value = LoginState.Error("登录失败: ${e.message}")
            }
        }
    }

    // 定义登录状态的几种可能状态
    sealed class LoginState {
        object Idle : LoginState() // 初始状态
        object Loading : LoginState() // 加载中
        data class Success(val nickname: String) : LoginState() // 登录成功，包含昵称
        data class Error(val message: String) : LoginState() // 登录失败，包含错误信息
    }
}
