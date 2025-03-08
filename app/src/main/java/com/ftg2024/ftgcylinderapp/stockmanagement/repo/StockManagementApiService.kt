package com.ftg2024.ftgcylinderapp.stockmanagement.repo

import com.ftg2024.ftgcylinderapp.stockmanagement.model.FilledStockDetailsResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StoreStockCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface StockManagementApiService {

    @PUT("/api/item/addRemoveStock")
    suspend fun addReturnStock(@Body request : StockUpdateRequest) : Response<StockUpdateResponse>

    @POST("/api/item/itemCount")
    suspend fun getStoreStockCount() : Response<StoreStockCountResponse>

    @POST("/api/stock/reportOfStockFilled")
    suspend fun getFillStockDetails() : Response<FilledStockDetailsResponse>

    @POST("/api/stock/reportOfStockRemoved")
    suspend fun getEmptyStockDetails() : Response<FilledStockDetailsResponse>
}