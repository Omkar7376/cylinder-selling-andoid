package com.ftg2024.ftgcylinderapp.admincustomermanagement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.admincustomermanagement.adapter.CustomerDetailsAdapter
import com.ftg2024.ftgcylinderapp.admincustomermanagement.adapter.CustomerListAdapter
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetailsResponse
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerListResponse
import com.ftg2024.ftgcylinderapp.admincustomermanagement.viewModel.CustomerManagementViewModel
import com.ftg2024.ftgcylinderapp.admincustomermanagement.viewModel.CustomerManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.databinding.ActivityCustomerManagementBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomerManagementActivity : AppCompatActivity(), CustomerListAdapter.OnCustomerClickListener {

    private lateinit var binding: ActivityCustomerManagementBinding
    private lateinit var progressDialog : ProgressDialog
    private var adapter: CustomerListAdapter?= null
    private var tableAdapter: CustomerDetailsAdapter?= null
    private var customerList: List<CustomerListResponse> = listOf()
    private var customerDetails: List<CustomerDetailsResponse> = listOf()

    @Inject
    lateinit var factory: CustomerManagementViewModelFactory

    private val viewmodel : CustomerManagementViewModel by viewModels{ factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerManagementBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog(this, "Loading...")
        setContentView(binding.root)
        setupObservers()
        setupRecyclerView()
        setOnClickListener()
        viewmodel.getCustomerDetails()
    }

    private fun setupRecyclerView() {
        binding.recyclerviewCustomerName.layoutManager = LinearLayoutManager(this)
        adapter = CustomerListAdapter(customerList, this)
        binding.recyclerviewCustomerName.adapter = adapter

        binding.recyclerviewCustomerDetails.layoutManager = LinearLayoutManager(this)
        tableAdapter = CustomerDetailsAdapter(customerDetails)
        binding.recyclerviewCustomerDetails.adapter = tableAdapter
    }

    private fun setOnClickListener() {
        binding.searchCustomer.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
            } else{
                binding.searchCustomer.clearFocus()
            }
        }
        binding.searchCustomer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val search = s.toString().trim()
                if (search.isEmpty()) {
                    binding.recyclerviewCustomerName.visibility = View.GONE
                } else {
                    binding.recyclerviewCustomerName.visibility = View.VISIBLE
                    getSearchResult(search)
                    viewmodel.getCustomerList(search)
                }
            }
        })
    }

    private fun getSearchResult(search : String) {
        val filteredList = customerList.filter {
            it.NAME.contains(search, ignoreCase = true)
        }
        adapter?.updateData(filteredList)
    }

    private  fun setupObservers() {
        viewmodel.customerListsMutableLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    val customerListResponse = response.data
                    Log.d("####", "setupObservers: $customerListResponse")
                    if (customerListResponse != null) {
                        if (customerListResponse.code == 200) {
                            val customerList = customerListResponse.data
                            Log.d("####", "Customer List: $customerList")
                            adapter?.updateData(customerList)
                        } else {
                            showToast("Something Went Wrong")
                        }
                    } else {
                        showToast("Something Went Wrong")
                    }
                }
                is Response.Error -> {
                    val error = response.exception
                    Log.e("####", "API Error: ${error?.message}")
                    showToast("Something Went Wrong")
                }
                else -> return@observe
            }
        }

        viewmodel.customerDetailsMutableLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    val customerDetailsResponse = response.data
                    Log.d("####", "setupObservers: $customerDetailsResponse")
                    if (customerDetailsResponse != null) {
                        if (customerDetailsResponse.code == 200) {
                            val customerList = customerDetailsResponse.data
                            Log.d("####", "Customer List: $customerList")
                            tableAdapter?.showData(customerList)
                        } else {
                            showToast("Something Went Wrong")
                        }
                    } else {
                        showToast("Something Went Wrong")
                    }
                }
                is Response.Error -> {
                    val error = response.exception
                    Log.e("####", "API Error: ${error?.message}")
                    showToast("Something Went Wrong")
                }
                else -> return@observe
            }
        }
    }

    private fun showToast(msg: String) {

    }

    override fun onCustomerCLicked(model: CustomerListResponse) {
    }
}