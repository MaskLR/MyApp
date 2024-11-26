package com.lyy.myapp.data.network

import retrofit2.http.GET

class ApiService {

}

interface IPv6ApiService {
    @GET("6.ipw.cn/")
    suspend fun getIPv6(): String
}