package com.ftg2024.ftgcylinderapp.dashboard.repo

import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsResponse
import retrofit2.Response
import retrofit2.http.POST

interface DashboardApiService {
    @POST("/api/item/get")
    suspend fun getCylinderDetails() : Response<CylinderDetailsResponse>
}