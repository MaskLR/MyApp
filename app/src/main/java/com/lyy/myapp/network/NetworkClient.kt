package com.lyy.myapp.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object NetworkClient {

    private val client = OkHttpClient()

    fun login(username: String, password: String, callback: (Result<String>) -> Unit) {
        val url = "http://mask.ddns.net:8888/api/loginUser.php"
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(IOException("Network error: ${e.message}")))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                val nickname = jsonResponse.optString("nickname", "Unknown")
                                callback(Result.success(nickname))
                            } else {
                                val message = jsonResponse.optString("message", "Unknown error")
                                callback(Result.failure(IOException("Server error: $message")))
                            }
                        } catch (e: JSONException) {
                            callback(Result.failure(IOException("Parsing error: ${e.message}")))
                        }
                    }
                } else {
                    callback(Result.failure(IOException("Server error: ${response.message}")))
                }
            }
        })
    }
}
