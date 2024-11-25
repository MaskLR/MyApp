package com.lyy.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.lyy.myapp.data.network.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MeViewModel : ViewModel() {
    // 定义界面状态
    data class UiState(
        val username: String = "",
        val password: String = "",
        val nickname: String = "",
        val isLoginMode: Boolean = true,
        val isLoading: Boolean = false,
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(UiState()) // 内部状态
    val uiState: StateFlow<UiState> get() = _uiState // 暴露只读状态

    // 更新输入框
    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onNicknameChange(newNickname: String) {
        _uiState.update { it.copy(nickname = newNickname) }
    }

    fun switchMode() {
        _uiState.update { it.copy(isLoginMode = !it.isLoginMode, message = null) }
    }

    fun performPrimaryAction() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank() || (!state.isLoginMode && state.nickname.isBlank())) {
            _uiState.update { it.copy(message = "所有字段都必须填写。") }
            return
        }

        _uiState.update { it.copy(isLoading = true, message = null) }

        if (state.isLoginMode) {
            // 调用登录逻辑
            NetworkClient.login(state.username, state.password) { success, message, nickname ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = if (success) "登录成功，你好 ${nickname ?: "用户"}！" else message ?: ""
                    )
                }
            }
        } else {
            // 调用注册逻辑
            NetworkClient.register(state.username, state.password, state.nickname) { success, message ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = if (success) "注册成功，请登录。" else message ?: ""
                    )
                }
                if (success) switchMode() // 注册成功后切换到登录模式
            }
        }
    }

}
