package com.ftg2024.ftgcylinderapp.distributionmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityDistributionDetailsBinding
import com.ftg2024.ftgcylinderapp.databinding.ActivityStockUpdateDetailsBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.PendingPaymentBottomFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionDetailsRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DistributionDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDistributionDetailsBinding
    private lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var factory : DistributionMgmtViewModelFactory

    private val viewModel : DistributionMgmtViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDistributionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Loading Data..")
        val bundle = intent.extras
        val id = bundle?.getInt("agent_id")
        progressDialog.show()
        viewModel.getAgentDistributionSummaryDetails(AgentDistributionDetailsRequest(id!!))
        setUpObserver()
        binding.buttonDistributionDetailsClearPayment.setOnClickListener {
            val bottomSheet = PendingPaymentBottomFragment.newInstance()
            bottomSheet.show(supportFragmentManager, "PendingPaymentBottomFragment")
        }
        binding.buttonDistributionDetailsCustomers.setOnClickListener {
            val intent = Intent(this, CustomerDistributionActivity::class.java)
            startActivity(intent)
        }
        binding.toolbarDistributionDetails.setNavigationOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    fun setUpObserver() {
        viewModel.agentDistributionSummaryDetailsLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    val agentDistributionDetails = response.data
                    if (agentDistributionDetails != null) {
                        binding.tvNameValue.text = agentDistributionDetails.agentInfo.NAME
                        binding.tvPhoneNumberValue.text = agentDistributionDetails.agentInfo.MOBILE_NO
                        binding.textviewDistributionDetailsAmount.text = agentDistributionDetails.pendingAmount.toString()
                        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                            Date()
                        )

                        binding.textviewDistributionDetailsDate.text = currentDate
                        for (item in agentDistributionDetails.itemDetails) {
                            if(item.ITEM_ID==1) {
                                binding.textviewDistributionDetailsCommercialCount.text = item.STOCK.toString()
                            } else if (item.ITEM_ID==2) {
                                binding.textviewDistributionDetailsDomesticCount.text = item.STOCK.toString()
                            } else if (item.ITEM_ID==3) {
                                binding.textviewDistributionDetailsMiniCount.text = item.STOCK.toString()
                            } else if (item.ITEM_ID==4) {
                                binding.textviewDistributionDetailsMiniDomCount.text = item.STOCK.toString()
                            }
                        }
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Error-> {
                    progressDialog.dismiss()
                    Toast.makeText(this, response.exception.message, Toast.LENGTH_SHORT).show()
                }
                else -> return@observe
            }
        }
    }
}