package com.lyy.myapp.domain.model

import com.lyy.myapp.data.network.LoginResponse

// 定义领域层的用户仓库接口，业务逻辑层通过它来调用具体的数据来源
interface UserRepository {
    suspend fun login(username: String, password: String): LoginResponse
}
