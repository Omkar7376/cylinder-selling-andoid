package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustDitributionRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustReturnRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerListResponse
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.repo.AgentCustDistributionRepo
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionSuccessResponse
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class AgentCustDistributionViewModel(private val repo: AgentCustDistributionRepo) : ViewModel() {
    private val _agentCustDetailsMutableLiveData : MutableLiveData<Response<AgentCustomerListResponse?>> = MutableLiveData()
    val agentCustDetailsMutableLiveData : LiveData<Response<AgentCustomerListResponse?>> get() = _agentCustDetailsMutableLiveData

    private val _agentCustDistributionMutableLiveData : MutableLiveData<Response<DistributionSuccessResponse?>> = MutableLiveData()
    val agentCustDistributionLiveData : LiveData<Response<DistributionSuccessResponse?>> get() = _agentCustDistributionMutableLiveData

    private val _agentCustReturnMutableLiveData : MutableLiveData<Response<DistributionSuccessResponse?>> = MutableLiveData()
    val agentCustReturnLiveData : LiveData<Response<DistributionSuccessResponse?>> get() = _agentCustReturnMutableLiveData


    fun getAgentCustList() {
        viewModelScope.launch {
            _agentCustDetailsMutableLiveData.value = repo.getAgentCustList()
        }
    }

    fun agentCustDistribution(request: AgentCustDitributionRequest) {
        viewModelScope.launch {
            _agentCustDistributionMutableLiveData.value = repo.agentCustDistri(request)
        }
    }

    fun agentCustReturn(request: AgentCustReturnRequest) {
        viewModelScope.launch {
            _agentCustReturnMutableLiveData.value = repo.agentCustReturn(request)
        }
    }
}
