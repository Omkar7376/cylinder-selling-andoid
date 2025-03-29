package com.ftg2024.ftgcylinderapp.auth.model

data class LoginRequest(
    val username: String,
    val password: String,
    val CLOUD_ID: String,
    val ROLE : String
)