package com.ftg2024.ftgcylinderapp.admincustomermanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ftg2024.ftgcylinderapp.admincustomermanagement.repo.CustomerManagementRepo
import javax.inject.Inject

class CustomerManagementViewModelFactory @Inject constructor(private val repo : CustomerManagementRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CustomerManagementViewModel(repo) as T
    }
}