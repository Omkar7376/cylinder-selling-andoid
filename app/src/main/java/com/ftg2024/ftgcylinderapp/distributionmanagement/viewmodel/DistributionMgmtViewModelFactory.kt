package com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ftg2024.ftgcylinderapp.distributionmanagement.repo.Distributionmgmtrepo
import javax.inject.Inject

class DistributionMgmtViewModelFactory @Inject constructor(private val repo : Distributionmgmtrepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return DistributionMgmtViewModel(repo) as T
    }
}