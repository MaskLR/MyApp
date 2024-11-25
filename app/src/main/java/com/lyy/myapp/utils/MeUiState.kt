package com.lyy.myapp.utils

/**
 * UI 状态，用于保存用户输入的信息和当前操作的状态。
 */
data class MeUiState(
    val username: String = "",
    val password: String = "",
    val nickname: String = "",
    val isLoginMode: Boolean = true, // true 表示登录模式，false 表示注册模式
    val isLoading: Boolean = false // 加载状态
)
