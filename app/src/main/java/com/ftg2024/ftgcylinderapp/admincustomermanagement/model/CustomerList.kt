package com.ftg2024.ftgcylinderapp.admincustomermanagement.model

data class CustomerList(
    val code: Int,
    val count: Int,
    val `data`: List<CustomerListResponse>,
    val message: String,
    val pages: Int
)