package com.ftg2024.ftgcylinderapp.stockmanagement.model

data class StockUpdateRequest(
    val StockDetails: List<StockDetail>,
    val INV_ERV_NO: String,
    val Type: String
)