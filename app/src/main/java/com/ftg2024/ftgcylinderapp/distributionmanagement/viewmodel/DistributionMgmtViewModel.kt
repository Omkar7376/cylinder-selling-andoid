package com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentReturnCylinderRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.repo.Distributionmgmtrepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class DistributionMgmtViewModel(private val repo : Distributionmgmtrepo) : ViewModel() {
    private val _agentDetailsMutableLiveData : MutableLiveData<Response<AgentDetailsResponse?>> = MutableLiveData()
    val agentDetailsMutableLiveData : LiveData<Response<AgentDetailsResponse?>> get() = _agentDetailsMutableLiveData

    private val _agentDistributionSummaryDetailsMutableLiveData : MutableLiveData<Response<AgentDistributionDetailsResponse?>> = MutableLiveData()
    val agentDistributionSummaryDetailsLiveData : LiveData<Response<AgentDistributionDetailsResponse?>> get() = _agentDistributionSummaryDetailsMutableLiveData


    private val _distributionMutableLiveData : MutableLiveData<Response<DistributionSuccessResponse?>> = MutableLiveData()
    val distributionMutableLiveData : LiveData<Response<DistributionSuccessResponse?>> get() = _distributionMutableLiveData

    private val _returnMutableLiveData : MutableLiveData<Response<DistributionSuccessResponse?>> = MutableLiveData()
    val returnMutableLiveData : LiveData<Response<DistributionSuccessResponse?>> get() = _returnMutableLiveData



    fun getAgentList() {
        viewModelScope.launch {
            _agentDetailsMutableLiveData.value = repo.getAgentList()
        }
    }

    fun getAgentDistributionSummaryDetails(request: AgentDistributionDetailsRequest) {
        viewModelScope.launch {
            _agentDistributionSummaryDetailsMutableLiveData.value = repo.getAgentDistributionSummaryDetails(request)
        }
    }

    fun distributeCylinder(request : AgentDistributionRequest) {
        viewModelScope.launch {
            _distributionMutableLiveData.value = repo.distributeCylinder(request)
        }
    }

    fun returnCylinder(request : AgentReturnCylinderRequest) {
        viewModelScope.launch {
            _returnMutableLiveData.value = repo.returnCylinder(request)
        }
    }
}