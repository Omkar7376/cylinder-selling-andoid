package com.ftg2024.ftgcylinderapp.stockmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockReturnConfirmationBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StockReturnConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockReturnConfirmationBinding
    private lateinit var request: StockUpdateRequest

    @Inject
    lateinit var factory: StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel> { factory }
    private lateinit var progressDialog: ProgressDialog
    private var isReturn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockReturnConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Updating Stock..")
        val bundle = intent.extras
        bundle!!.getBoolean("is_return")
        setValues()
        setUpObserver()
        setOnClickListener()
    }

    private fun setUpObserver() {
        viewModel.updateStockLiveData.observe(this) { response ->
            when (response) {
                is Response.Success -> {
                    Log.d("####", "Response: $response")
                    progressDialog.dismiss()
                    Toast.makeText(this, "Stock Updated Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@StockReturnConfirmationActivity, DashboardActivity::class.java))
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, response.exception.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@StockReturnConfirmationActivity, DashboardActivity::class.java))
                }

                else -> {
                    return@observe
                }
            }
        }
    }

    private fun setValues() {
        val stockReturnRequestJson = intent.getStringExtra("stock_return_request_json")
        val isReturn = intent.getBooleanExtra("is_return", false)
        Log.d("####", "setValues: $isReturn")
        val gson = Gson()
        val stockReturnRequest: StockUpdateRequest? = stockReturnRequestJson?.let {
            gson.fromJson(it, StockUpdateRequest::class.java)
        }
        if (stockReturnRequest != null) {
            request = stockReturnRequest
        } else {
            Log.e("StockReturnConfirmationActivity", "Error: Received null stock request")
            return
        }
        binding.textviewActivityStockReturnConfirmationDate.text = stockReturnRequest.Date
        binding.textviewActivityStockReturnConfirmationErvNo.text = stockReturnRequest.INV_ERV_NO

        request.StockDetails.forEach { stock ->
            when(stock.ID){
                1 -> {
                    binding.textviewActivityStockReturnConfirmation19kgEmpty.text = stock.Value.toString()
                    binding.textviewActivityStockReturnConfirmation19kgDefective.text = stock.Defective.toString()
                }
                2 -> {
                    binding.textviewActivityStockReturnConfirmation142kgEmpty.text = stock.Value.toString()
                    binding.textviewActivityStockReturnConfirmation142kgDefective.text = stock.Defective.toString()
                }
                3 -> {
                    binding.textviewActivityStockReturnConfirmation5kgComEmpty.text = stock.Value.toString()
                    binding.textviewActivityStockReturnConfirmation5kgComDefective.text = stock.Defective.toString()
                }
                4 -> {
                    binding.textviewActivityStockReturnConfirmation5kgDomEmpty.text = stock.Value.toString()
                    binding.textviewActivityStockReturnConfirmation5kgDomDefective.text = stock.Defective.toString()
                }
            }
        }
    }

    private fun setOnClickListener(){
        binding.buttonConfirmationSubmit.setOnClickListener {
            progressDialog.show()
            viewModel.addRemoveStock(request)
            Log.d("####", "addRemoveStock: $viewModel")
        }
    }
}