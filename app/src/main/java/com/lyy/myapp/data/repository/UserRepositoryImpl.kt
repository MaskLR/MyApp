package com.lyy.myapp.data.repository

import com.lyy.myapp.data.network.LoginResponse
import com.lyy.myapp.data.remote.RemoteDataSource
import com.lyy.myapp.domain.model.UserRepository
import javax.inject.Inject

// 数据仓库实现类，具体实现登录操作
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    // 登录实现，调用远程数据源的 login 方法
    override suspend fun login(username: String, password: String): LoginResponse {
        return remoteDataSource.login(username, password)
    }
}
