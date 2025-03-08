package com.ftg2024.ftgcylinderapp.stockmanagement

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ftg2024.ftgcylinderapp.databinding.ActivityUpdateStockBinding
import com.ftg2024.ftgcylinderapp.stockmanagement.fragment.StockConfirmationBottomSheetFragment
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockDetail
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class UpdateStockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateStockBinding
    private var isReturn = false
    private var type = "F"
    private val currentDate  = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd MMM yyyy : hh:mm a", Locale.getDefault())
    private val formattedDate: String = dateFormat.format(currentDate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateStockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundleValues()
        binding.textviewUpdateStockDateTime.text = formattedDate
        if (isReturn) {
            binding.editTextUpdateActivityNumber.hint = "Enter ERV Number"
            type = "R"
        } else {
            binding.editTextUpdateActivityNumber.hint = "Enter Invoice Number"
        }
        setOnCLickListeners()
    }

    private fun setOnCLickListeners() {
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
    }
}