package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model

data class AgentCustomerListResponse(
    val code: Int,
    val count: Int,
    val `data`: List<AgentCustomerData>,
    val message: String,
    val pages: Int
)