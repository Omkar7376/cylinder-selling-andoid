package com.ftg2024.ftgcylinderapp.stockmanagement.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ftg2024.ftgcylinderapp.databinding.FragmentStockConfirmationBottomSheetBinding
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StockConfirmationBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var formattedDate: String

    @Inject
    lateinit var factory: StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel> { factory }
    private lateinit var binding: FragmentStockConfirmationBottomSheetBinding
    private lateinit var request: StockUpdateRequest
    private lateinit var listener : OnStockStatusUpdateListener
    private var isReturn = false
    private var isFailed = false
    private var isClear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStockConfirmationBottomSheetBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.labelStockConfirmationNumber.text = if (isReturn) "ERV Number" else "Invoice Number"
        setValues()
        setOnCLickListeners()
        setUpObservers()
        return binding.root
    }

    private fun setValues() {
        val commercialCylinderStock = request.StockDetails[0].Value
        val domesticCylinderStock = request.StockDetails[1].Value
        val miniCylinderStock = request.StockDetails[2].Value
        binding.textviewStockConfirmationCommercialCylinder.text = commercialCylinderStock.toString()
        binding.textviewStockConfirmationDomesticCylinder.text = domesticCylinderStock.toString()
        binding.textviewStockConfirmationMiniCylinder.text = miniCylinderStock.toString()
        binding.textviewStockConfirmationNumber.text = request.INV_ERV_NO
        binding.textviewStockConfirmationDateTime.text = formattedDate
        binding.textviewStockConfirmationTotalQuantity.text = (commercialCylinderStock + domesticCylinderStock + miniCylinderStock).toString()
    }
    private fun setOnCLickListeners() {
        binding.btnConfirm.setOnClickListener {
            showProgressBar()
            viewModel.addRemoveStock(request)
        }
    }

    private fun setUpObservers() {
        viewModel.updateStockLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Success -> {
                    val stockUpdateResponse = response.data
                    hideProgressBar()
                    if (stockUpdateResponse!= null) {
                        if (stockUpdateResponse.code == 200) {
                            showToast("Stock Updated Successfully")
                            isClear = true
                            dismiss()
                        } else if (stockUpdateResponse.code == 304) {
                            val statusList = stockUpdateResponse.updated
                            val resultList = mutableListOf<Int>()
                            for(i in statusList.indices) {
                                val stockStatus = statusList[i]
                                if (stockStatus.COUNT < request.StockDetails[i].Value){
                                    resultList.add(stockStatus.COUNT)
                                } else {
                                    resultList.add(0)
                                }
                            }
                            listener.stockUpdated(resultList)
                            showToast("Stock Unavailable")
                            isFailed = true
                            isClear = false
                            dismiss()
                        } else {
                            showToast("Something Went Wrong")
                        }
                    } else {
                        showToast("Something Went Wrong")
                    }
                }

                is Response.Error -> {
                    val error = response.exception
                    showToast("Something Went Wrong")
                    hideProgressBar()
                }

                else -> return@observe
            }
        }
    }

    private fun showProgressBar() {
        binding.containerStockConfirmationConfirmRequest.visibility = View.INVISIBLE
        binding.progressbarStockConfirmationConfirmRequest.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.containerStockConfirmationConfirmRequest.visibility = View.VISIBLE
        binding.progressbarStockConfirmationConfirmRequest.visibility = View.INVISIBLE
    }

    private fun showToast(msg : String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("####", "onDismiss: $isClear $isFailed")
        listener.onDismiss(isClear, isFailed)
    }

    interface OnStockStatusUpdateListener {
        fun stockUpdated(stockCountList : List<Int>)
        fun onDismiss(isClear : Boolean, isFailed : Boolean)
    }
    companion object {
        @JvmStatic
        fun newInstance(onStockStatusUpdateListener: OnStockStatusUpdateListener,request: StockUpdateRequest, isReturn: Boolean, formattedDate: String) =
            StockConfirmationBottomSheetFragment().apply {
                this.isReturn = isReturn
                this.request = request
                this.listener = onStockStatusUpdateListener
                this.formattedDate = formattedDate
            }
    }
}