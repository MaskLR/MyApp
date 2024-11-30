package com.lyy.myapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users") // 定义 Room 表名
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int
)