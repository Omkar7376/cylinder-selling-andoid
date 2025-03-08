package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentDetailsResponse(
    val code: Int,
    val count: Int,
    val data: List<AgentData>,
    val message: String,
    val pages: Int
)