package com.ftg2024.ftgcylinderapp.stockmanagement.model

data class StoreStockCountResponse(
    val code: Int,
    val count: Int,
    val data: List<StoreStockData>,
    val message: String,
    val pages: Int
)