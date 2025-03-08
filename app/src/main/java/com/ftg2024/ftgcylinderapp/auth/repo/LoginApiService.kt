package com.ftg2024.ftgcylinderapp.auth.repo

import com.ftg2024.ftgcylinderapp.auth.model.LoginRequest
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST



interface LoginApiService {
    @POST("/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}