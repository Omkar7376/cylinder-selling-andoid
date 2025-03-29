package com.ftg2024.ftgcylinderapp.stockmanagement.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.databinding.FragmentStockConfirmationBottomSheetBinding
import com.ftg2024.ftgcylinderapp.stockmanagement.adapter.StockConfirmationAdapter
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StockConfirmationBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var formattedDate: String

    @Inject
    lateinit var factory: StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel> { factory }
    private lateinit var binding: FragmentStockConfirmationBottomSheetBinding
    lateinit var request: StockUpdateRequest
    //lateinit var listener : OnStockStatusUpdateListener
    var isReturn = false
    private var isFailed = false
    private var isClear = false

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStockConfirmationBottomSheetBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.labelConfirmationInvErvNo.text = if (isReturn) "ERV Number" else "Invoice Number"
        setValues()
        setOnCLickListeners()
        //setUpObservers()
        return binding.root
    }

    private fun setValues() {
        val adapter = StockConfirmationAdapter(request.StockDetails)
        binding.recyclerViewStockConfirmation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        binding.tvConfirmationInvErvNo.text = request.INV_ERV_NO
        binding.tvConfirmationVehicleNo.text = request.VEHICLE_NO
        binding.tvConfirmationDate.text = formatDate(request.Date)
    }

   private fun setOnCLickListeners() {
        binding.buttonConfirmationSubmit.setOnClickListener {
            dismiss()
            //listener.onSubmit()
        }
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date ?: return inputDate)  // Returns original if parsing fails
    }
    /* private fun setUpObservers() {
        viewModel.updateStockLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Success -> {
                    val stockUpdateResponse = response.data

                    Log.d("####", "setUpObservers: $stockUpdateResponse")

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
                    Log.d("####", "setUpObservers: ${error.message}")
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
    }*/

    private fun showToast(msg : String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("####", "onDismiss: $isClear $isFailed")
        //listener.onDismiss(isClear, isFailed)
    }

    interface OnStockStatusUpdateListener {
        fun onSubmit()
        fun stockUpdated(stockCountList : List<Int>)
        fun onDismiss(isClear : Boolean, isFailed : Boolean)
    }
    /*companion object {
        @JvmStatic
        fun newInstance(onStockStatusUpdateListener: OnStockStatusUpdateListener, request: StockUpdateRequest, isReturn: Boolean) =
            StockConfirmationBottomSheetFragment().apply {
                this.isReturn = isReturn
                this.request = request
                //this.listener = onStockStatusUpdateListener
            }
    }*/
}