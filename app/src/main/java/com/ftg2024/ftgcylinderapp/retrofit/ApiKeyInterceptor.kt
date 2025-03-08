package com.ftg2024.ftgcylinderapp.retrofit

import com.ftg2024.ftgcylinderapp.constants.ApiConstants.API_KEY
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    //private val apiKey: String = "hjh4653dsiivy457468asdfe"
    /*@Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request: Request = chain.request()
        val newRequest: Request = request.newBuilder()
            .addHeader("apikey", "$apiKey")
            .build()
        return chain.proceed(newRequest)
    }*/
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request()
        val newRequest: Request = request.newBuilder()
            .addHeader("apikey", "$API_KEY")
            .build()
        return chain.proceed(newRequest)
    }
}