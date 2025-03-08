package com.ftg2024.ftgcylinderapp.auth.model

data class LoginResponse(
    val code: Int,
    val data: List<LoginResponseData>,
    val message: String
)