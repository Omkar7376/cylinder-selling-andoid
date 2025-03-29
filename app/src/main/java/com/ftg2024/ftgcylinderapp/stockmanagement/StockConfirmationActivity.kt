package com.ftg2024.ftgcylinderapp.stockmanagement

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockConfirmationBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.fragment.StockConfirmationBottomSheetFragment
import com.ftg2024.ftgcylinderapp.stockmanagement.fragment.StockConfirmationBottomSheetFragment.OnStockStatusUpdateListener
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StockConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockConfirmationBinding
    private lateinit var request: StockUpdateRequest

    @Inject
    lateinit var factory: StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel> { factory }
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Updating Stock..")
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
                    startActivity(Intent(this@StockConfirmationActivity, DashboardActivity::class.java))
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, response.exception.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@StockConfirmationActivity, DashboardActivity::class.java))
                }

                else -> {
                    return@observe
                }
            }
        }
    }

    private fun setValues() {
        val stockRequestJson = intent.getStringExtra("stock_request_json")
        val isReturn = intent.getBooleanExtra("is_return", false)
        Log.d("####", "setValues: $isReturn")
        val gson = Gson()
        val stockRequest: StockUpdateRequest? = stockRequestJson?.let {
            gson.fromJson(it, StockUpdateRequest::class.java)
        }
        if (stockRequest != null) {
            request = stockRequest
        } else {
            Log.e("StockConfirmationActivity", "Error: Received null stock request")
            return
        }
        binding.textviewActivityStockConfirmationDate.text = stockRequest.Date
        binding.textviewActivityStockConfirmationInvoice.text = stockRequest.INV_ERV_NO
        binding.textviewActivityStockConfirmationTruckNo.text = stockRequest.VEHICLE_NO

        request.StockDetails.forEach { stock ->
            when(stock.ID){
                1 -> {
                    binding.textviewActivityStockConfirmation19kgFull.text = stock.Value.toString()
                    binding.textviewActivityStockConfirmation19kgDefective.text = stock.Defective.toString()
                }
                2 -> {
                    binding.textviewActivityStockConfirmation142kgFull.text = stock.Value.toString()
                    binding.textviewActivityStockConfirmation142kgDefective.text = stock.Defective.toString()
                }
                3 -> {
                    binding.textviewActivityStockConfirmation5kgComFull.text = stock.Value.toString()
                    binding.textviewActivityStockConfirmation5kgComDefective.text = stock.Defective.toString()
                }
                4 -> {
                    binding.textviewActivityStockConfirmation5kgDomFull.text = stock.Value.toString()
                    binding.textviewActivityStockConfirmation5kgDomDefective.text = stock.Defective.toString()
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