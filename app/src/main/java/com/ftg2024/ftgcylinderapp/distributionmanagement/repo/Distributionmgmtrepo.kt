package com.ftg2024.ftgcylinderapp.distributionmanagement.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentReturnCylinderRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
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

    suspend fun getAgentDistributionSummaryDetails(request: AgentDistributionDetailsRequest): Response<AgentDistributionDetailsResponse?> {
        return try {
            val response = apiService.getAgentDistributionSummaryDetails(request)
            Log.d("####", "getAgentDistributionSummaryDetails: $response")
            if (response.isSuccessful) {
                val agentDistributionDetailsResponse = response.body()
                Log.d("####", "getAgentDistributionSummaryDetails: $agentDistributionDetailsResponse")
                if (agentDistributionDetailsResponse != null) {
                    if (agentDistributionDetailsResponse.code == 200) {
                        Response.Success(response.body())
                    } else {
                        Response.Error(Exception("Something Went Wrong"), response.code())
                    }
                } else {
                    Response.Error(Exception("Something Went Wrong"), response.code())
                }
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }

    suspend fun distributeCylinder(request : AgentDistributionRequest): Response<DistributionSuccessResponse?> {
        return try {
            val response = apiService.distributeCylinder(request)
            Log.d("####", "distributeCylinder: $response")
            if (response.isSuccessful) {
                val distributionSuccessResponse = response.body()
                Log.d("####", "getAgentList: $distributionSuccessResponse")
                if (distributionSuccessResponse != null) {
                    if (distributionSuccessResponse.code == 200) {
                        Response.Success(response.body())
                    } else {
                        Response.Error(Exception("Something Went Wrong"), response.code())
                    }
                } else {
                    Response.Error(Exception("Something Went Wrong"), response.code())
                }
            } else {
                if (response.code()==400){
                    Response.Error(Exception("Something Went Wrong"), response.code())
                }
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }

    suspend fun returnCylinder(request : AgentReturnCylinderRequest): Response<DistributionSuccessResponse?> {
        return try {
            val response = apiService.returnCylinder(request)
            Log.d("####", "returnCylinder: $response")
            if (response.isSuccessful) {
                val distributionSuccessResponse = response.body()
                Log.d("####", "getAgentList: $distributionSuccessResponse")
                if (distributionSuccessResponse != null) {
                    if (distributionSuccessResponse.code == 200) {
                        Response.Success(response.body())
                    } else {
                        Response.Error(Exception("Something Went Wrong"), response.code())
                    }
                } else {
                    Response.Error(Exception("Something Went Wrong"), response.code())
                }
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(Exception(e.message), null)
        }
    }
}