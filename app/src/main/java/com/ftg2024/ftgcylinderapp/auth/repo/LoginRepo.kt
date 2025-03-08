package com.ftg2024.ftgcylinderapp.auth.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.auth.model.LoginRequest
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponse
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponseData
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject

class LoginRepo @Inject constructor(private val apiService: LoginApiService) {

    suspend fun login(request : LoginRequest) : Response<LoginResponse> {
        return try {
            val response = apiService.login(request)
            Log.d("####", "login: $response")
            if (response.isSuccessful) {
                return Response.Success(response.body()!!)
            } else {
                return Response.Error(Exception(response.message()), response.code())
            }
        } catch(e : Exception) {
            return  Response.Error(Exception(e), null)
        }
    }
}