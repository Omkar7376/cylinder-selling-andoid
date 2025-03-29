package com.ftg2024.ftgcylinderapp

import android.app.Application
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp  : Application(){
    var cylinderDetailsData = mutableListOf<CylinderDetailsData>()

}