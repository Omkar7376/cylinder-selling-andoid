package com.ftg2024.ftgcylinderapp.dashboard.repo

import android.util.Log
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsResponse
import com.ftg2024.ftgcylinderapp.uidata.Response
import javax.inject.Inject
import kotlin.math.log

class DashboardRepo @Inject constructor(private val apisService : DashboardApiService) {

    suspend fun getCylinderDetails() : Response<CylinderDetailsResponse> {
        return try {
            val response = apisService.getCylinderDetails()
            Log.d("####", "getCylinderDetails: $response")
            if (response.isSuccessful) {
                val cylinderDetailsResponse = response.body()
                Log.d("####", "getCylinderDetails: $cylinderDetailsResponse")
                if (cylinderDetailsResponse != null) {
                    if (cylinderDetailsResponse.code == 200) {
                        Response.Success(cylinderDetailsResponse)
                    } else {
                        Response.Error(Exception("Something Went Wrong"), response.code())
                    }
                    //Response.Success(cylinderDetailsResponse)
                } else {
                    Response.Error(Exception("Something Went Wrong"), response.code())
                }
            } else {
                Response.Error(Exception("Something Went Wrong"), response.code())
            }
        } catch(e : Exception) {
            Log.d("####", "getCylinderDetails: $e")
            Response.Error(Exception("Something Went Wrong"), null)
        }
    }
}