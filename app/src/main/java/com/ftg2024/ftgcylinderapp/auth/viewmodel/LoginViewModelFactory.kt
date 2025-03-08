package com.ftg2024.ftgcylinderapp.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ftg2024.ftgcylinderapp.auth.repo.LoginRepo
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(val repo: LoginRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return LoginViewmodel(repo) as T
    }
}