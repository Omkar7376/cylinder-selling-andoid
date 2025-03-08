package com.ftg2024.ftgcylinderapp.distributionmanagement.model

data class AgentStockResponse(
    val code: Int,
    val count: Int,
    val data: List<AgentStockResponseData>,
    val message: String,
    val pages: Int
)