package com.ftg2024.ftgcylinderapp.stockmanagement.model

data class StockUpdateRequest(
    val Date: String,
    val INV_ERV_NO: String,
    val StockDetails: List<StockDetail>,
    val Type: String,
    val VEHICLE_NO: String
)