package com.lyy.myapp.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import android.content.Context
import android.content.SharedPreferences

/**
 * NetworkClient 是一个用于处理网络请求的对象，负责用户的登录和注册操作。
 */
object NetworkClient {
    private const val PREFS_NAME = "myapp_prefs" // SharedPreferences 文件名，用于存储应用配置
    private const val TOKEN_KEY = "auth_token"   // 存储认证 token 的键

    private lateinit var sharedPreferences: SharedPreferences // 用于存储和检索认证 token
    private val client = OkHttpClient() // OkHttpClient 实例，用于执行 HTTP 请求

    /**
     * 初始化 NetworkClient。此方法必须在使用其他功能之前调用。
     * @param context 上下文，用于获取 SharedPreferences
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 执行登录操作。
     * @param username 用户名
     * @param password 密码
     * @param callback 登录结果回调，传入登录是否成功、错误信息（如果有）和服务器返回的昵称（如果有）
     */
    fun login(username: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        val url = "http://mask.ddns.net:8888/api/loginUser.php" // 登录请求的 URL

        // 构建 JSON 请求体
        val jsonObject = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val mediaType = "application/json".toMediaType() // 请求体的媒体类型
        val requestBody = jsonObject.toString().toRequestBody(mediaType) // 创建请求体

        // 构建 HTTP 请求
        val request = Request.Builder()
            .url(url) // 设置请求的 URL
            .post(requestBody) // 设置请求体
            .build() // 构建请求

        // 执行异步 HTTP 请求
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 登录请求失败时的处理
                callback(false, e.message, null) // 调用回调函数，传递失败信息
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    // 检查响应是否成功
                    if (!response.isSuccessful) {
                        callback(false, "Unexpected code $response", null) // 调用回调函数，传递错误信息
                        return
                    }

                    // 处理成功的响应
                    val responseData = response.body?.string() // 获取响应的内容
                    val jsonResponse = JSONObject(responseData) // 将响应内容转换为 JSON 对象
                    val success = jsonResponse.getBoolean("success") // 获取登录是否成功的标志
                    val message = jsonResponse.optString("message") // 获取服务器返回的消息
                    val nickname = jsonResponse.optString("nickname") // 获取服务器返回的昵称

                    // 调用回调函数，传递登录结果、消息和昵称
                    callback(success, message, if (success) nickname else null)
                }
            }
        })
    }

    /**
     * 执行注册操作。
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param callback 注册结果回调，传入注册是否成功和错误信息（如果有）
     */
    fun register(username: String, password: String, nickname: String, callback: (Boolean, String?) -> Unit) {
        val url = "http://mask.ddns.net:8888/api/registerUser.php" // 注册请求的 URL

        // 构建 JSON 请求体
        val jsonObject = JSONObject().apply {
            put("nickname", nickname)
            put("username", username)
            put("password", password)
        }

        val mediaType = "application/json".toMediaType() // 请求体的媒体类型
        val requestBody = jsonObject.toString().toRequestBody(mediaType) // 创建请求体

        // 构建 HTTP 请求
        val request = Request.Builder()
            .url(url) // 设置请求的 URL
            .post(requestBody) // 设置请求体
            .build() // 构建请求

        // 执行异步 HTTP 请求
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 注册请求失败时的处理
                callback(false, e.message) // 调用回调函数，传递失败信息
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    // 检查响应是否成功
                    if (!response.isSuccessful) {
                        callback(false, "Unexpected code $response") // 调用回调函数，传递错误信息
                        return
                    }

                    // 处理成功的响应
                    val responseData = response.body?.string() // 获取响应的内容
                    val jsonResponse = JSONObject(responseData) // 将响应内容转换为 JSON 对象
                    val status = jsonResponse.getString("status") // 获取注册状态
                    val message = jsonResponse.getString("message") // 获取服务器返回的消息

                    // 调用回调函数，传递注册结果和消息
                    callback(status == "success", message)
                }
            }
        })
    }
}
