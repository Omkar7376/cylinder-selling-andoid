package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model

data class AgentCustReturnRequest(
    val AGENT_ID: Int,
    val CUSTOMER_ID: Int,
    val ORDER_DATETIME: String,
    val orderData: List<OrderData>
)