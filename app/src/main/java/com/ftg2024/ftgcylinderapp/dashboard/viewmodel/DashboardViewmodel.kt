package com.ftg2024.ftgcylinderapp.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsResponse
import com.ftg2024.ftgcylinderapp.dashboard.repo.DashboardRepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class DashboardViewmodel(private val repo : DashboardRepo) : ViewModel() {
    private val _cylinderDetailsMutableLiveData : MutableLiveData<Response<CylinderDetailsResponse>> = MutableLiveData()
    val cylinderDetailsLiveData : LiveData<Response<CylinderDetailsResponse>> = _cylinderDetailsMutableLiveData

    fun getCylinderDetails() {
        viewModelScope.launch {
            _cylinderDetailsMutableLiveData.value = repo.getCylinderDetails()
        }
    }
}