package com.ftg2024.ftgcylinderapp.distributionmanagement.repo

import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DistributionMgmtApiService {
    @POST("/api/agent/get")
    suspend fun getAgentList()  : Response<AgentDetailsResponse>
}