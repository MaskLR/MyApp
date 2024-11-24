package com.lyy.myapp.data.network

import org.json.JSONObject
import java.io.IOException
import android.content.Context
import android.content.SharedPreferences
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * NetworkClient 是一个用于处理网络请求的对象，负责用户的登录和注册操作。
 */
object NetworkClient {

    private const val PREFS_NAME = "myapp_prefs" // SharedPreferences 文件名，用于存储应用配置
    private const val TOKEN_KEY = "auth_token"   // 存储认证 token 的键

    private lateinit var sharedPreferences: SharedPreferences // 用于存储和检索认证 token
    private val client = OkHttpClient() // OkHttpClient 实例，用于执行 HTTP 请求

    /**
     * 初始化 NetworkClient。
     * 此方法必须在使用其他功能之前调用。
     * @param context 上下文，用于获取 SharedPreferences。
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 执行登录操作。
     * @param username 用户名。
     * @param password 密码。
     * @param callback 登录结果回调，返回登录是否成功、错误信息（如果有）和服务器返回的昵称（如果有）。
     */
    fun login(username: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        val url = "http://mask.ddns.net:808/api/loginUser.php" // 登录请求的 URL

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
                // 登录请求失败时调用回调函数并传递错误信息
                callback(false, e.message ?: "网络请求失败", null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        // 响应失败，调用回调函数传递错误信息
                        callback(false, "服务器错误: $response", null)
                        return
                    }

                    // 解析响应数据
                    val responseData = response.body?.string() ?: "{}" // 获取响应内容，默认空 JSON
                    val jsonResponse = JSONObject(responseData) // 将响应转换为 JSON 对象
                    val success = jsonResponse.optBoolean("success", false) // 登录是否成功
                    val message = jsonResponse.optString("message", "未知错误") // 提供默认消息
                    val nickname = jsonResponse.optString("nickname", "") // 昵称字段

                    // 调用回调函数，返回结果
                    callback(success, message, if (success) nickname else null)
                }
            }
        })
    }

    /**
     * 执行注册操作。
     * @param username 用户名。
     * @param password 密码。
     * @param nickname 昵称。
     * @param callback 注册结果回调，返回注册是否成功和错误信息（如果有）。
     */
    fun register(username: String, password: String, nickname: String, callback: (Boolean, String?) -> Unit) {
        val url = "http://mask.ddns.net:808/api/registerUser.php" // 注册请求的 URL

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
                // 注册请求失败时调用回调函数并传递错误信息
                callback(false, e.message ?: "网络请求失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        // 响应失败，调用回调函数传递错误信息
                        callback(false, "服务器错误: $response")
                        return
                    }

                    // 解析响应数据
                    val responseData = response.body?.string() ?: "{}" // 获取响应内容，默认空 JSON
                    val jsonResponse = JSONObject(responseData) // 将响应转换为 JSON 对象
                    val status = jsonResponse.optString("status", "error") // 注册状态
                    val message = jsonResponse.optString("message", "未知错误") // 提供默认消息

                    // 调用回调函数，返回结果
                    callback(status == "success", message)
                }
            }
        })
    }
}
