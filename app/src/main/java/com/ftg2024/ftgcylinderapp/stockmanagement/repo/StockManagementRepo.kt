package com.ftg2024.ftgcylinderapp.stockmanagement.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.stockmanagement.model.FilledStockDetailsResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StoreStockCountResponse
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject

class StockManagementRepo @Inject constructor(private val apiService: StockManagementApiService) {

    suspend fun addRemoveStock(request : StockUpdateRequest): Response<StockUpdateResponse?> {
        return try {
            val response = apiService.addReturnStock(request)
            Log.d("####", "addRemoveStock: $response")
            if (response.isSuccessful) {
                val updateStockResponse = response.body()
                Log.d("####", "addRemoveStock: $updateStockResponse")
                if (updateStockResponse != null) {
                    if (updateStockResponse.code == 200) {
                        Response.Success(response.body())
                    } else if(updateStockResponse.code == 301) {
                        Response.Error(Exception("Entered Value is Greater than Stock"), response.code())
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

    suspend fun getStoreStockCountResponse() : Response<StoreStockCountResponse?> {
        return try {
            val response = apiService.getStoreStockCount()
            Log.d("####", "getStoreStockCountResponse: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(e, null)
        }
    }

    suspend fun getFilledStockList() : Response<FilledStockDetailsResponse?> {
        return try {
            val response = apiService.getFillStockDetails()
            Log.d("####", "getFilledStockList: $response")
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(e, null)
        }
    }

    suspend fun getEmptyStockList() : Response<FilledStockDetailsResponse?> {
        return try {
            val response = apiService.getEmptyStockDetails()
            if (response.isSuccessful) {
                Response.Success(response.body())
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch (e : Exception) {
            Response.Error(e, null)
        }
    }
}