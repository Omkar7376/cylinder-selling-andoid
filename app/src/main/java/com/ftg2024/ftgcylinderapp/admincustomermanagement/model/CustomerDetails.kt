package com.ftg2024.ftgcylinderapp.admincustomermanagement.model

data class CustomerDetails(
    val code: Int,
    val count: Int,
    val `data`: List<CustomerDetailsResponse>,
    val message: String,
    val pages: Int
)