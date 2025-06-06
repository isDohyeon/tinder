package hnu.multimedia.tinder.util

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyFirebaseMessagingSender {

    private val SERVER_IP = "172.30.1.76"
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun sendFCM(targetToken: String, title: String, body: String) {
        val url = "http://${SERVER_IP}:80/send-fcm"

        val json = JSONObject().apply {
            put("targetToken", targetToken)
            put("title", title)
            put("body", body)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        // okhttp3 를 import
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("MyFirebaseMessagingSender", "FCM success")
                } else {
                    Log.d("MyFirebaseMessagingSender", "FCM failure: ${response.body?.string()}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}