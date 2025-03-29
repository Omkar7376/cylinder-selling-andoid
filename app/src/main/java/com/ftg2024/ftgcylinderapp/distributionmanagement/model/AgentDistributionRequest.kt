package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentDistributionRequest(
    val AGENT_ID: Int,
    val orderData: List<DistributionData>,
    val ORDER_DATETIME : String
)