package com.ftg2024.ftgcylinderapp.auth.model

data class LoginResponseData(
    val UserData: List<UserData>,
    val token: String
)