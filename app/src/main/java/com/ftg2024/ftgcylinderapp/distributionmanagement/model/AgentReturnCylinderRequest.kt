package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentReturnCylinderRequest(
    val agentId: Int,
    val assignedList: List<ReturnRequestData>
)