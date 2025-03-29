package com.ftg2024.ftgcylinderapp.admincustomermanagement.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetails
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetailsResponse
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerList
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject

class CustomerManagementRepo @Inject constructor(private val apiService : CustomerManagementApiService){
    suspend fun getCustomerList(search: String) : Response<CustomerList?>{
        return try {
            val response = apiService.getCustomerLists()
            Log.d("####", "getCustomerLists: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }

    suspend fun getCustomerDetails() : Response<CustomerDetails?> {
        return try {
            val response = apiService.getCustomerDetails()
            Log.d("####", "getCustomerDetails: $response")
            if(response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }
}