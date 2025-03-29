package com.ftg2024.ftgcylinderapp.admincustomermanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetails
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerList
import com.ftg2024.ftgcylinderapp.admincustomermanagement.repo.CustomerManagementRepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class CustomerManagementViewModel(private val repo : CustomerManagementRepo) : ViewModel() {
    private val _customerListsMutableLiveData : MutableLiveData<Response<CustomerList?>> = MutableLiveData()
    val customerListsMutableLiveData : LiveData<Response<CustomerList?>> get() = _customerListsMutableLiveData

    fun getCustomerList(search: String) {
        viewModelScope.launch {
            _customerListsMutableLiveData.value = repo.getCustomerList(search)
        }
    }

    private val _customerDetailsMutableLiveData : MutableLiveData<Response<CustomerDetails?>> = MutableLiveData()
    val customerDetailsMutableLiveData : LiveData<Response<CustomerDetails?>> get() = _customerDetailsMutableLiveData

    fun getCustomerDetails() {
        viewModelScope.launch {
            _customerDetailsMutableLiveData.value = repo.getCustomerDetails()
        }
    }
}