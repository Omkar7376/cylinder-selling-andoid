package com.ftg2024.ftgcylinderapp.stockmanagement

import android.app.DatePickerDialog
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.ftg2024.ftgcylinderapp.databinding.ActivityUpdateStockBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.fragment.StockConfirmationBottomSheetFragment
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockDetail
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class UpdateStockActivity : AppCompatActivity() {
    private lateinit var request: StockUpdateRequest
    private lateinit var binding: ActivityUpdateStockBinding
    private var isReturn = false
    private var type = "F"
    private val currentDate  = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val formattedDate: String = dateFormat.format(currentDate)

    private var domesticCylinderQyt = 0
    private var domesticCylinderDefQyt = 0

    private var commercialCylinderQyt = 0
    private var commercialCylinderDefQyt = 0

    private var weldingCylinderQyt = 0
    private var weldingCylinderDefQyt = 0

    private var miniCylinderQyt = 0
    private var miniCylinderDefQyt = 0

    private var invErvNo : String? = ""
    private var vehicleNo : String? = ""
    private var date : String? = ""
    private var textDate : String? = ""

    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var factory: StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel> { factory }
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateStockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Updating Stock..")
        getBundleValues()
        if (isReturn) {
            binding.labelUdateStocksInvErvNumber.text = "Enter ERV Number"
            binding.labelUdateStocksVehicleNumber.visibility = View.GONE
            binding.etUdateStocksVehicleNumber.visibility = View.GONE
            binding.toolbarAddNewStock.title = "Return Cylinder Stock"
            type = "R"
        } else {
            binding.labelUdateStocksInvErvNumber.text = "Enter Invoice Number"
            binding.toolbarAddNewStock.title = "Add Cylinder Stock"
        }
        setOnCLickListeners()

        request = intent.getParcelableExtra("stock_request") ?: return
        isReturn = intent.getBooleanExtra("is_return", false)
        setUpObserver()
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isReturn = bundle?.getBoolean("is_return") ?: false
        Log.d("####", "getBundleValues: $isReturn")
    }

    private fun setOnCLickListeners() {
        binding.buttonUpdateStockQuantitySubmit.setOnClickListener {
            if (validate()) {
                binding.etMiniCylinderQuantity.apply {
                    miniCylinderQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etMiniCylinderQuantityDefective.apply {
                    miniCylinderDefQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etCommercialCylinderQuantity.apply {
                    commercialCylinderQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etCommercialCylinderQuantityDefective.apply {
                    commercialCylinderDefQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etWeldingCylinderQuantity.apply {
                    weldingCylinderQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etWeldingCylinderQuantityDefective.apply {
                    weldingCylinderDefQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etDomesticCylinderQuantity.apply {
                    domesticCylinderQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                binding.etDomesticCylinderQuantityDefective.apply {
                    domesticCylinderDefQyt = if (text.isNullOrEmpty()) 0 else text.toString().toInt()
                }
                invErvNo = binding.etUdateStocksInvoiceNumber.text.toString()
                vehicleNo = binding.etUdateStocksVehicleNumber.text.toString()
                val stockDetailList = mutableListOf<StockDetail>()
                stockDetailList.add(StockDetail(1,commercialCylinderQyt,commercialCylinderDefQyt))
                stockDetailList.add(StockDetail(2,domesticCylinderQyt, domesticCylinderDefQyt))
                stockDetailList.add(StockDetail(3,miniCylinderQyt, miniCylinderDefQyt))
                stockDetailList.add(StockDetail(4, weldingCylinderQyt, weldingCylinderDefQyt))
                request = StockUpdateRequest(date!!, invErvNo!!, stockDetailList, type, vehicleNo!!)
                showStockConfirmation()
            }
        }

        binding.tvUdateStocksDateTime.apply {
            setOnClickListener {
                val datePicker = DatePickerDialog(this@UpdateStockActivity, {_, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    //textDateFormat(calendar.time)
                    text = textDateFormat(calendar.time)
                    date = dateFormat.format(calendar.time)
                },  calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun setUpObserver() {
        viewModel.updateStockLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    cleartexts()
                    Toast.makeText(this, "Stock Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, response.exception.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    return@observe
                }
            }
        }
    }

    private fun showStockConfirmation() {
        if(isReturn){
            val gson = Gson()
            val stockReturnRequestJson = gson.toJson(request)

            val intent = Intent(this,StockReturnConfirmationActivity::class.java).apply {
                putExtra("stock_return_request_json", stockReturnRequestJson)
                putExtra("isReturn",isReturn)
            }
            startActivity(intent)
        } else {
            val gson = Gson()
            val stockRequestJson = gson.toJson(request)

            val intent = Intent(this, StockConfirmationActivity::class.java).apply {
                putExtra("stock_request_json", stockRequestJson)
            }
            startActivity(intent)
        }
    }


    /*private fun showStockConfirmation() {
        val bottomSheetFragment = StockConfirmationActivity.newInstance(object :
            StockConfirmationActivity.OnStockStatusUpdateListener {
            override fun onSubmit() {
                progressDialog.show()
                viewModel.addRemoveStock(request)
            }

            override fun stockUpdated(stockCountList: List<Int>) {
                for(i in stockCountList.indices) {
                    if (stockCountList[i] > 0) {

                    }
                }
            }

            override fun onDismiss(isClear: Boolean, isFailed : Boolean) {
                if (!isClear && isFailed) {

                }

                if (isClear) {

                }
            }

        },request, isReturn)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }*/

    private fun cleartexts() {
        binding.etUdateStocksInvoiceNumber.text?.clear()
        binding.etUdateStocksVehicleNumber.text?.clear()
        binding.tvUdateStocksDateTime.text = "Select Date and Time"
        binding.etCommercialCylinderQuantity.text?.clear()
        binding.etDomesticCylinderQuantity.text?.clear()
        binding.etMiniCylinderQuantity.text?.clear()
        binding.etWeldingCylinderQuantity.text?.clear()
        binding.etCommercialCylinderQuantityDefective.text?.clear()
        binding.etDomesticCylinderQuantityDefective.text?.clear()
        binding.etMiniCylinderQuantityDefective.text?.clear()
        binding.etWeldingCylinderQuantityDefective.text?.clear()
    }

    private fun textDateFormat(date: Date): String {
        return dateFormat.format(date)
    }
   /* private fun showConfirmationBottomSheet() {
        val bottomSheetFragment = StockConfirmationBottomSheetFragment.newInstance(object :
            StockConfirmationBottomSheetFragment.OnStockStatusUpdateListener {
            override fun stockUpdated(stockCountList: List<Int>) {
                for(i in stockCountList.indices) {
                    if (stockCountList[i] > 0) {
                        if ( i== 0) {
                            binding.labelErrorCommercial.apply {
                                visibility = View.VISIBLE
                                text = "Available Stock : ${stockCountList[i]}"
                            }
                        } else if (i == 1) {
                            binding.labelErrorDomestic.apply {
                                visibility = View.VISIBLE
                                text = "Available Stock : ${stockCountList[i]}"
                            }
                        } else {
                            binding.labelErrorMini.apply {
                                visibility = View.VISIBLE
                                text = "Available Stock : ${stockCountList[i]}"
                            }
                        }
                    }
                }
            }

            override fun onDismiss(isClear: Boolean, isFailed : Boolean) {
                if (!isClear && isFailed) {
                    binding.labelErrorCommercial.visibility = View.VISIBLE
                    binding.labelErrorDomestic.visibility = View.VISIBLE
                    binding.labelErrorMini.visibility = View.VISIBLE
                }

                if (isClear) {
                    binding.etCommercialCylinderQuantity.setText("0")
                    binding.etDomesticCylinderQuantity.setText("0")
                    binding.etMiniLpgQuantity.setText("0")
                }
            }

        },request, isReturn, formattedDate)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }*/
    private fun validate() : Boolean {
        if (binding.etUdateStocksVehicleNumber.text.isNullOrEmpty() && !isReturn)
        {
            binding.etUdateStocksVehicleNumber.error = "This field is required"
            return false
        }
        if (binding.etUdateStocksInvoiceNumber.text.isNullOrEmpty())
        {
            binding.etUdateStocksInvoiceNumber.error = "This field is required"
            return false
        }
        if (binding.tvUdateStocksDateTime.text == "Select Date and Time")
        {
            Toast.makeText(this, "Please Select Date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*private fun setOnCLickListeners() {
        binding.buttonUpdateStockSubmit.setOnClickListener {
            val listOfStockDetail = mutableListOf<StockDetail>()

            listOfStockDetail.add(
                StockDetail(
                    1,
                    binding.etCommercialCylinderQuantity.text.toString().takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
                )
            )
            listOfStockDetail.add(
                StockDetail(
                    2,
                    binding.etDomesticCylinderQuantity.text.toString().takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
                )
            )
            listOfStockDetail.add(
                StockDetail(
                    3,
                    binding.etMiniLpgQuantity.text.toString().takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
                )
            )
            if (validate()) {
                binding.labelErrorCommercial.visibility = View.GONE
                binding.labelErrorDomestic.visibility = View.GONE
                binding.labelErrorMini.visibility = View.GONE
                val request = StockUpdateRequest(listOfStockDetail, binding.editTextUpdateActivityNumber.text.toString(),type)
                val bottomSheetFragment = StockConfirmationBottomSheetFragment.newInstance(object :
                    StockConfirmationBottomSheetFragment.OnStockStatusUpdateListener {
                    override fun stockUpdated(stockCountList: List<Int>) {
                        for(i in stockCountList.indices) {
                            if (stockCountList[i] > 0) {
                                if ( i== 0) {
                                    binding.labelErrorCommercial.apply {
                                        visibility = View.VISIBLE
                                        text = "Available Stock : ${stockCountList[i]}"
                                    }
                                } else if (i == 1) {
                                    binding.labelErrorDomestic.apply {
                                        visibility = View.VISIBLE
                                        text = "Available Stock : ${stockCountList[i]}"
                                    }
                                } else {
                                    binding.labelErrorMini.apply {
                                        visibility = View.VISIBLE
                                        text = "Available Stock : ${stockCountList[i]}"
                                    }
                                }
                            }
                        }
                    }

                    override fun onDismiss(isClear: Boolean, isFailed : Boolean) {
                        if (!isClear && isFailed) {
                            binding.labelErrorCommercial.visibility = View.VISIBLE
                            binding.labelErrorDomestic.visibility = View.VISIBLE
                            binding.labelErrorMini.visibility = View.VISIBLE
                        }

                        if (isClear) {
                            binding.etCommercialCylinderQuantity.setText("0")
                            binding.etDomesticCylinderQuantity.setText("0")
                            binding.etMiniLpgQuantity.setText("0")
                        }
                    }

                },request, isReturn, formattedDate)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            } else {
                val msg = if (type == "R") "ERV Number" else "Invoice Number"
                Toast.makeText(this,"Please enter $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isReturn = bundle?.getBoolean("is_return") ?: false
        Log.d("####", "getBundleValues: $isReturn")
    }

    private fun validate() : Boolean {
        if (binding.editTextUpdateActivityNumber.text.isNullOrEmpty())
            return false
        return true
    }*/
}