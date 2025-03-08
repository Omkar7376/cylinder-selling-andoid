package com.ftg2024.ftgcylinderapp.retrofit

import android.util.Log
import com.ftg2024.ftgcylinderapp.sharedprefs.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenKeyInterceptor @Inject constructor(private val tokenManager : SharedPrefManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("####", "intercept: ${tokenManager.getToken()}")
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("token", "${tokenManager.getToken()}")
            .build()
        return chain.proceed(newRequest)
    }
}