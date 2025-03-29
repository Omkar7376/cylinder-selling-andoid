package com.ftg2024.ftgcylinderapp.admincustomermanagement.repo

import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetails
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerList
import retrofit2.Response
import retrofit2.http.POST

interface CustomerManagementApiService {
    @POST("/api/Customer/get")
    suspend fun getCustomerLists() : Response<CustomerList>

    @POST("/api/itemCustomer/getByCustomer")
    suspend fun getCustomerDetails() : Response<CustomerDetails>
}