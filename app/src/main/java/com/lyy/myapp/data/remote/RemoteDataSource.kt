package com.lyy.myapp.data.remote

import com.lyy.myapp.data.network.ApiService
import com.lyy.myapp.data.network.LoginRequest
import javax.inject.Inject

// 远程数据源，用于和后端服务器进行交互
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    // 登录操作，通过调用 ApiService 的 login 方法发起请求
    suspend fun login(username: String, password: String) =
        apiService.login(LoginRequest(username, password))
}
