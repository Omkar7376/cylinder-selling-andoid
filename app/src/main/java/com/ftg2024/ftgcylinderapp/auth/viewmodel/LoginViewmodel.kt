package com.ftg2024.ftgcylinderapp.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.auth.model.LoginRequest
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponse
import com.ftg2024.ftgcylinderapp.auth.repo.LoginRepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class LoginViewmodel(val repo: LoginRepo) : ViewModel() {
    private val _loginMutableLiveData : MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    val loginLiveData : LiveData<Response<LoginResponse>> get() = _loginMutableLiveData

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            val response = repo.login(request)
            _loginMutableLiveData.value = response
        }
    }

}