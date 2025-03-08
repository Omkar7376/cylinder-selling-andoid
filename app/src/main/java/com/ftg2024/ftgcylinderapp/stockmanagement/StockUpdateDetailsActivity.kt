package com.ftg2024.ftgcylinderapp.stockmanagement


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockUpdateDetailsBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.adapter.StockUpdateAdapter
import com.ftg2024.ftgcylinderapp.stockmanagement.model.FilledStockDetailsResponse
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModel
import com.ftg2024.ftgcylinderapp.stockmanagement.viewmodel.StockManagementViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import javax.inject.Inject


@AndroidEntryPoint
class StockUpdateDetailsActivity : AppCompatActivity() {
    private lateinit var adapter: StockUpdateAdapter
    private lateinit var binding: ActivityStockUpdateDetailsBinding
    private lateinit var progressDialog : ProgressDialog

    private var isFilled : Boolean = true

    @Inject
    lateinit var factory : StockManagementViewModelFactory

    private val viewModel by viewModels<StockManagementViewModel>{ factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityStockUpdateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundleValues()
        progressDialog = ProgressDialog(this, "Loading...")
        progressDialog.show()
        binding.recyclerviewStockUpdateDetails.layoutManager = LinearLayoutManager(this)

        if (isFilled) {
            viewModel.getFilledStockDetailsList()
        } else {
            viewModel.getEmptyStockDetailsList()
        }
        setUpObservers()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    private fun setUpObservers() {
        viewModel.filledStockDetailsMutableLivedata.observe(this){ response ->
            handleResponse(response)
        }

        viewModel.emptyStockDetailsMutableLivedata.observe(this) {response ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response<FilledStockDetailsResponse?>) {
        when(response) {
            is Response.Success -> {
                progressDialog.dismiss()
                val filledStockDetailsResponse = response.data
                Log.d("####", "setUpObservers: $filledStockDetailsResponse")
                if (filledStockDetailsResponse != null ) {
                    if (filledStockDetailsResponse.code == 200) {
                        val filledStockDetailsList = filledStockDetailsResponse.data
                        if (!filledStockDetailsList.isNullOrEmpty()) {
                            Log.d("###", "isFilled value: ${filledStockDetailsList.size}")
                            adapter = StockUpdateAdapter(filledStockDetailsList, isFilled)
                            binding.recyclerviewStockUpdateDetails.adapter = adapter
                        } else {
                            showToast("No data to Fetch")
                        }
                    } else {
                        showToast("Something Went Wrong")
                    }
                } else {
                    showToast("Something Went Wrong")
                }
            }

            is Response.Error -> {
                progressDialog.dismiss()
                val exception = response.exception
                if (exception is SocketTimeoutException) {
                    showToast("Server Timeout retry")
                } else {
                    showToast("Something Went Wrong")
                }
            }
            else -> return
        }
    }

    fun showDatePickerDialog(view: View?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val customView: View = layoutInflater.inflate(com.ftg2024.ftgcylinderapp.R.layout.date_filter_dialog, null)
        builder.setView(customView)
        builder.create().show()
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isFilled = bundle!!.getBoolean("is_filled")
    }

    private fun showToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}