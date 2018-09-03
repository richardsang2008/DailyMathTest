package com.dailymathtest.joy.dailymath.controllers

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

open class NetworkClient {
    fun fetchJson(url:String): String {
        val request = Request.Builder().url(url).build()
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        var retStr=""
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("********************************Reach here")
                val body = response?.body()?.string()
                retStr = body!!


            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Failure to excute method")
            }
        })
        return retStr
    }
}
