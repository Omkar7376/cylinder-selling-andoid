package com.ftg2024.ftgcylinderapp.admincustomermanagement.model

data class CustomerDetailsResponse(
    val CUSTOMER_ID: Int,
    val CUSTOMER_NAME: String,
    val MOBILE_NO: String,
    val customer_data: List<CustomerCylinderDetails>
)