package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentAssignedCylinderDetailsResponse(
    val code: Int,
    val count: Int,
    val `data`: List<AgentAssignedCylinderDetailData>,
    val message: String,
    val pages: Int
)