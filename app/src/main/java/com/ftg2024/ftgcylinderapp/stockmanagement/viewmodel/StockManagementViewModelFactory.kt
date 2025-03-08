package com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftg2024.ftgcylinderapp.stockmanagement.repo.StockManagementRepo
import javax.inject.Inject

class StockManagementViewModelFactory @Inject constructor(private val repo : StockManagementRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StockManagementViewModel(repo) as T
    }
}