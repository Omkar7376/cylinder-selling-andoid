package com.ftg2024.ftgcylinderapp.distributionmanagement

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
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentDistributionBinding
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentDistributionConfirmationBinding
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockConfirmationBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AgentDistributionConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentDistributionConfirmationBinding
    private lateinit var request: AgentDistributionRequest

    @Inject
    lateinit var factory: DistributionMgmtViewModelFactory

    private val viewmodel: DistributionMgmtViewModel by viewModels { factory }

    private lateinit var progressDialog: ProgressDialog
    private var isReturn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentDistributionConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Loading...")
        val bundle = intent.extras
        bundle!!.getBoolean("is_return")
        setValues()
        setUpObservers()
        setUpClickListener()
    }

    private fun setUpObservers() {
        viewmodel.distributionMutableLiveData.observe(this) { response ->
            when (response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Cylinder Distributed Successfully", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@AgentDistributionConfirmationActivity, DashboardActivity::class.java))
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AgentDistributionConfirmationActivity, DashboardActivity::class.java))

                }

                else -> return@observe
            }
        }
    }

    private fun setValues(){
        val distributionRequestJson = intent.getStringExtra("distribution_request_json")
        val isReturn = intent.getBooleanExtra("is_return", false)
        Log.d("####", "setValues: $isReturn")
        val name = intent.getStringExtra("Delivery_Boy_Name")
        val gson = Gson()
        val distributionRequest: AgentDistributionRequest? = distributionRequestJson?.let {
            gson.fromJson(it, AgentDistributionRequest::class.java)
        }
        if (distributionRequest != null) {
            request = distributionRequest
        } else {
            return
        }
        binding.textviewActivityAgentDistributionConfirmationDate.text = distributionRequest.ORDER_DATETIME
        binding.textviewActivityAgentDistributionConfirmationDeliveryBoyName.text = name.toString()

        request.orderData.forEach { stock ->
            when(stock.ITEM_ID){
                1 -> {
                    binding.textViewActivityAgentDistributionConfirmation19kgFull.text = stock.QTY.toString()
                }
                2 -> {
                    binding.textViewActivityAgentDistributionConfirmation142kgFull.text = stock.QTY.toString()
                }
                3 -> {
                    binding.textViewActivityAgentDistributionConfirmation5kgComFull.text = stock.QTY.toString()
                }
                4 -> {
                    binding.textViewActivityAgentDistributionConfirmation5kgDomFull.text = stock.QTY.toString()
                }
            }
        }
    }

    private fun setUpClickListener(){
        binding.buttonConfirmationSubmit.setOnClickListener {
            progressDialog.show()
            viewmodel.distributeCylinder(request)
            Log.d("####", "request: $request")
        }
    }
}
