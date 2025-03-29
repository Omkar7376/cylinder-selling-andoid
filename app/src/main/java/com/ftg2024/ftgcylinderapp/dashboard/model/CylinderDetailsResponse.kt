package com.ftg2024.ftgcylinderapp.dashboard.model

data class CylinderDetailsResponse(
    val code: Int,
    val count: Int,
    val data: List<CylinderDetailsData>,
    val message: String,
    val pages: Int
)