package com.ftg2024.ftgcylinderapp.distributionmanagement.repo

import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentAssignedCylinderDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentReturnCylinderRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DistributionMgmtApiService {
    @POST("/api/agent/get")
    suspend fun getAgentList()  : Response<AgentDetailsResponse>

    @POST("/api/order/createOrder")
    suspend fun distributeCylinder(@Body request : AgentDistributionRequest) : Response<DistributionSuccessResponse>

    @POST("/api/order/createReturnOrder")
    suspend fun returnCylinder(@Body request : AgentReturnCylinderRequest) : Response<DistributionSuccessResponse>

    @POST("/api/order/pendingamount")
    suspend fun getAgentDistributionSummaryDetails(@Body request: AgentDistributionDetailsRequest) : Response<AgentDistributionDetailsResponse>
}