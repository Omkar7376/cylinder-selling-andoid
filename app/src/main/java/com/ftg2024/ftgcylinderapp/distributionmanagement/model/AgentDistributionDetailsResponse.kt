package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentDistributionDetailsResponse(
    val agentId: Int,
    val agentInfo: AgentDistributionInfo,
    val code: Int,
    val itemDetails: List<ItemDetail>,
    val pendingAmount: Int
)