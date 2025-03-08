package com.ftg2024.ftgcylinderapp.distributionmanagement.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject

class Distributionmgmtrepo @Inject constructor(private val apiService : DistributionMgmtApiService) {
    suspend fun getAgentList(): Response<AgentDetailsResponse?> {
        return try {
            val response = apiService.getAgentList()
            Log.d("####", "getAgentList: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }
}