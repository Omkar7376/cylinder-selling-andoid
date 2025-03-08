package com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDetailsResponse
import com.ftg2024.ftgcylinderapp.distributionmanagement.repo.Distributionmgmtrepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class DistributionMgmtViewModel(private val repo : Distributionmgmtrepo) : ViewModel() {
    private val _agentDetailsMutableLiveData : MutableLiveData<Response<AgentDetailsResponse?>> = MutableLiveData()
    val agentDetailsMutableLiveData : LiveData<Response<AgentDetailsResponse?>> get() = _agentDetailsMutableLiveData

    fun getAgentList() {
        viewModelScope.launch {
            _agentDetailsMutableLiveData.value = repo.getAgentList()
        }
    }
}