package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentStockResponseData(
    val AGENT_ID: Int,
    val AGENT_NAME: String,
    val agent_data: List<AgentStockData>
)