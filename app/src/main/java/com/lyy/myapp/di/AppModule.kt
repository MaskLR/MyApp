package com.lyy.myapp.di

import com.lyy.myapp.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class) // 全局注入
object AppModule {

    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://your.api.url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

//    @Provides
//    fun provideUserDao(context: Context): UserDao {
//        // 假设你已经在Room数据库中定义了UserDao
//        return Room.databaseBuilder(context, YourDatabase::class.java, "database-name")
//            .build()
//            .userDao()
//    }
//
//    @Provides
//    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
//        return RemoteDataSource(apiService)
//    }
//
//    @Provides
//    fun provideUserRepository(userDao: UserDao, remoteDataSource: RemoteDataSource): UserRepository {
//        return UserRepositoryImpl(userDao, remoteDataSource)
//    }
}
