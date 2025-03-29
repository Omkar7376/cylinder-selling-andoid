package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustDitributionRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustReturnRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerListResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject

class AgentCustDistributionRepo @Inject constructor(private val apiService: AgentCustDistributionApiService) {

    suspend fun getAgentCustList(): Response<AgentCustomerListResponse?>{
        return try {
            val response = apiService.getAgentCustList()
            Log.d("####", "getAgentCustList: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }

    suspend fun agentCustDistri(request: AgentCustDitributionRequest): Response<DistributionSuccessResponse?>{
        return try {
            val response = apiService.agentCustDistibute(request)
            Log.d("####", "agentCustDistri: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }

    suspend fun agentCustReturn(request: AgentCustReturnRequest): Response<DistributionSuccessResponse?>{
        return try {
            val response = apiService.agentCustReturn(request)
            Log.d("####", "agentCustReturn: $response")
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
