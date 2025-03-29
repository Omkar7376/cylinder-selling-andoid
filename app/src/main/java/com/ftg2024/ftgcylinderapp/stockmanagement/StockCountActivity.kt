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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockCountBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StockCountActivity : AppCompatActivity() {

    @Inject
    lateinit var factory : StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel>{ factory }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityStockCountBinding
    private val currentDate  = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd MMM yyyy : hh:mm a", Locale.getDefault())
    private val formattedDate: String = dateFormat.format(currentDate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityStockCountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Loading Data...")
        progressDialog.show()
        viewModel.getStoreStockCount()
        setUpObservers()
        setOnCLickListeners()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    private fun setOnCLickListeners() {
        binding.textviewStockCountFilledViewDetails.setOnClickListener {
           navigateToDetails(true)
        }

        binding.textviewStockCountEmptyViewDetails.setOnClickListener {
            navigateToDetails(false)
        }
    }

    private fun setUpObservers() {
        viewModel.storeStockMutableLivedata.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    val stockCountResponse = response.data
                    Log.d("####", "setUpObservers: $stockCountResponse")
                    if (stockCountResponse != null) {
                        if (stockCountResponse.code == 200) {
                            val stockCountData = stockCountResponse.data
                            val commercialCountList = stockCountData[3]
                            val domesticCountList = stockCountData[2]
                            val miniCountList = stockCountData[1]
                            val bcmgCountList = stockCountData[0]
                            //Filled Stock Count
                            binding.textviewStockCountCommercialCount.text = commercialCountList.STOCK_FILLED.toString()
                            binding.textviewStockCountDomesticCount.text = domesticCountList.STOCK_FILLED.toString()
                            binding.textviewStockCountMiniCount.text = miniCountList.STOCK_FILLED.toString()
                            binding.textviewStockCountBcmgCount.text = bcmgCountList.STOCK_FILLED.toString()
                            binding.textviewStockCountTotalCount.text = (commercialCountList.STOCK_FILLED + domesticCountList.STOCK_FILLED + miniCountList.STOCK_FILLED).toString()
                            binding.textviewStockCountDate.text = formattedDate

                            //Empty Stock Count
                            binding.textviewStockCountEmptyCommercialCount.text = commercialCountList.STOCK_EMPTY.toString()
                            binding.textviewStockCountEmptyDomesticCount.text = domesticCountList.STOCK_EMPTY.toString()
                            binding.textviewStockCountEmptyMiniCount.text = miniCountList.STOCK_EMPTY.toString()
                            binding.textviewStockCountEmptyBcmgCount.text = bcmgCountList.STOCK_EMPTY.toString()
                            binding.textviewStockCountEmptyTotalCount.text = (commercialCountList.STOCK_EMPTY + domesticCountList.STOCK_EMPTY + miniCountList.STOCK_EMPTY).toString()
                            binding.textviewStockEmptyCountDate.text = formattedDate

                        } else {
                            showToast("Something Went Wrong")
                        }
                    } else {
                        showToast("Something Went Wrong")
                    }
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    showToast("Something Went Wrong")
                }
                else -> return@observe
            }
        }
    }


    private fun navigateToDetails(value : Boolean) {
        Log.d("###", "navigateToDetails: ")
        val intent = Intent(this, StockUpdateDetailsActivity::class.java)
        intent.putExtra("is_filled", value)
        startActivity(intent)
    }

    private fun showToast(msg : String) {
        Log.d("###","Hello")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}