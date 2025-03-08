package com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftg2024.ftgcylinderapp.stockmanagement.model.FilledStockDetailsResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StoreStockCountResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.repo.StockManagementRepo
import com.ftg2024.ftgcylinderapp.uidata.Response
import kotlinx.coroutines.launch

class StockManagementViewModel(val repo : StockManagementRepo) : ViewModel() {
    private val _updateStockMutableLiveData : MutableLiveData<Response<StockUpdateResponse?>> = MutableLiveData()
    val updateStockLiveData : LiveData<Response<StockUpdateResponse?>> get() = _updateStockMutableLiveData

    private val _storeStockMutableLivedata : MutableLiveData<Response<StoreStockCountResponse?>> = MutableLiveData()
    val storeStockMutableLivedata : LiveData<Response<StoreStockCountResponse?>> get() = _storeStockMutableLivedata

    private val _filledStockDetailsMutableLivedata : MutableLiveData<Response<FilledStockDetailsResponse?>> = MutableLiveData()
    val filledStockDetailsMutableLivedata : LiveData<Response<FilledStockDetailsResponse?>> get() = _filledStockDetailsMutableLivedata

    private val _emptyStockDetailsMutableLivedata : MutableLiveData<Response<FilledStockDetailsResponse?>> = MutableLiveData()
    val emptyStockDetailsMutableLivedata : LiveData<Response<FilledStockDetailsResponse?>> get() = _emptyStockDetailsMutableLivedata

     fun addRemoveStock(request : StockUpdateRequest) {
        viewModelScope.launch{
            _updateStockMutableLiveData.value = repo.addRemoveStock(request)
        }
    }

    fun getStoreStockCount() {
        viewModelScope.launch {
            _storeStockMutableLivedata.value = repo.getStoreStockCountResponse()
        }
    }

    fun getFilledStockDetailsList() {
        viewModelScope.launch {
            _filledStockDetailsMutableLivedata.value = repo.getFilledStockList()
        }
    }

    fun getEmptyStockDetailsList() {
        viewModelScope.launch {
            _emptyStockDetailsMutableLivedata.value = repo.getEmptyStockList()
        }
    }
}