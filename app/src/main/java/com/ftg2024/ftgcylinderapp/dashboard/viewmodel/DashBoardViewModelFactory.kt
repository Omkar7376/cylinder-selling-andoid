package com.ftg2024.ftgcylinderapp.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ftg2024.ftgcylinderapp.dashboard.repo.DashboardRepo
import javax.inject.Inject

class DashBoardViewModelFactory @Inject constructor(val repo: DashboardRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return DashboardViewmodel(repo) as T
    }
}