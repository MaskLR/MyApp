package com.lyy.myapp.data.network

import retrofit2.http.Body
import retrofit2.http.POST

// 定义登录请求的数据模型
data class LoginRequest(val username: String, val password: String)

// 定义登录响应的数据模型
data class LoginResponse(val nickname: String)

// 定义网络请求接口
interface ApiService {

    // POST 请求接口，用于登录操作
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
