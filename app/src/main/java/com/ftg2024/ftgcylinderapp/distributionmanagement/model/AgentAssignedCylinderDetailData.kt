package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentAssignedCylinderDetailData(
    val AGENT_ID: Int,
    val AGENT_NAME: String,
    val agent_data: List<AgentAssignedData>
)