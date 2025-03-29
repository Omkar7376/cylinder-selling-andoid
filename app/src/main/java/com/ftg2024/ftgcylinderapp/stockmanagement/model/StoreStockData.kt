package com.ftg2024.ftgcylinderapp.stockmanagement.model

data class StoreStockData(
    val NAME: String,
    val STOCK_EMPTY: Int,
    val STOCK_FILLED: Int,
    val TOTAL_STOCK: Int,
    val STOCK_DEFECTIVE : Int
)