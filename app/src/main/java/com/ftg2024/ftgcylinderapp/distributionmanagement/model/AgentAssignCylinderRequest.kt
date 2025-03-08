package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentAssignCylinderRequest(
    val agentId: Int,
    val assignedList: List<AssignRequestData>
)