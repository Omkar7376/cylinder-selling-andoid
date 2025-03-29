package com.ftg2024.ftgcylinderapp.stockmanagement.model

data class FilledStockDetailsResponse(
    val code: Int,
    val count: Int,
    val data: List<StockDetailsData>,
    val message: String,
    val pages: Int
)