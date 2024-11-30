package com.lyy.myapp.di

import com.lyy.myapp.data.network.ApiService
import com.lyy.myapp.data.remote.RemoteDataSource
import com.lyy.myapp.data.repository.UserRepositoryImpl
import com.lyy.myapp.domain.model.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // 在应用程序级别安装此模块
object AppModule {

    // 提供 ApiService 的实例，Retrofit 实例化时通过此方法获取
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://mask.ddns.net:808/api/loginUser.php/") // 替换为后端的真实地址
            .addConverterFactory(GsonConverterFactory.create()) // 解析 JSON 响应
            .build()
            .create(ApiService::class.java)
    }

    // 提供 RemoteDataSource 的实例
    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    // 提供 UserRepository 的实例，业务逻辑层通过此仓库获取数据
    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource): UserRepository {
        return UserRepositoryImpl(remoteDataSource)
    }
}
