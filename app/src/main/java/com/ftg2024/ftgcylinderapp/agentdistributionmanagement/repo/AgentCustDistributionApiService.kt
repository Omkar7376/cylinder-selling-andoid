package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.repo

import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustDitributionRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustReturnRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerListResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AgentCustDistributionApiService {
    @POST("/api/customer/get")
    suspend fun getAgentCustList() : Response<AgentCustomerListResponse>

    @POST("/api/agent/order/createOrder")
    suspend fun agentCustDistibute(@Body request: AgentCustDitributionRequest) : Response<DistributionSuccessResponse>

    @POST("/api/agent/order/createOrder")
    suspend fun agentCustReturn(@Body request: AgentCustReturnRequest) : Response<DistributionSuccessResponse>


}