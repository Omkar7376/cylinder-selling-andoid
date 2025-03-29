package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.repo.AgentCustDistributionRepo
import javax.inject.Inject

class AgentCustDistributionViewModelFactory @Inject constructor(private val repo : AgentCustDistributionRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return AgentCustDistributionViewModel(repo) as T
    }
}