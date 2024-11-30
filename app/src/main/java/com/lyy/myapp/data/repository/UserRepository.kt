package com.lyy.myapp.data.repository

import com.lyy.myapp.domain.model.User

interface UserRepository {
    suspend fun loginUser(username: String, password: String): User
}