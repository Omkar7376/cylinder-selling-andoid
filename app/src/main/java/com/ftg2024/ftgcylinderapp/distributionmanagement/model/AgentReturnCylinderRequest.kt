package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentReturnCylinderRequest(
    val AGENT_ID: Int,
    val ORDER_DATETIME: String,
    val PAID_AMOUNT: Int,
    val orderData: List<ReturnData>
)