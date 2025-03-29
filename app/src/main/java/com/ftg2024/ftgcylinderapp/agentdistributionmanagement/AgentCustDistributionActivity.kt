package com.ftg2024.ftgcylinderapp.agentdistributionmanagement

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.MyApp
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.fragment.AgentCustomerList
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustDitributionRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustOrderData
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerData
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModel
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModelFactory
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentCustDistributionBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.sharedprefs.SharedPrefManager
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AgentCustDistributionActivity : AppCompatActivity() {

    @Inject
    lateinit var factory : AgentCustDistributionViewModelFactory

    @Inject
    lateinit var prefManager: SharedPrefManager

    private val viewModel : AgentCustDistributionViewModel by viewModels { factory }
    private lateinit var binding: ActivityAgentCustDistributionBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var request : AgentCustDitributionRequest
    private lateinit var agentCustDistributionList: MutableList<AgentCustOrderData>
    private lateinit var cylindersDetails : List<CylinderDetailsData>

    private val app: MyApp by lazy { application as MyApp }

    private var custId = -1
    private var agentId = -1
    private var serverDate : String = ""
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    private var domesticCylinderQyt = 0

    private var commercialCylinderQyt = 0

    private var miniDomCylinderQyt = 0

    private var miniComCylinderQyt = 0

    private var totAmount = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentCustDistributionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        agentId = prefManager.getLoginResponseData()!![0].UserData[0].USER_ID
        setOnClickListener()
        setUpObservers()
        cylindersDetails = app.cylinderDetailsData
        progressDialog = ProgressDialog(this,"Loading...")
    }

    private fun setOnClickListener() {
        binding.buttonAgentCustDistributionSubmit.setOnClickListener {
            if (validate()) {
                agentCustDistributionList = mutableListOf()
                agentCustDistributionList.add(AgentCustOrderData(0, 1, commercialCylinderQyt))
                agentCustDistributionList.add(AgentCustOrderData(0, 2, domesticCylinderQyt))
                agentCustDistributionList.add(AgentCustOrderData(0, 3, miniComCylinderQyt))
                agentCustDistributionList.add(AgentCustOrderData(0, 4, miniDomCylinderQyt))
                request = AgentCustDitributionRequest(agentId, custId, serverDate, agentCustDistributionList)
                viewModel.agentCustDistribution(request)
                progressDialog.show()
            }
        }

        binding.searchAgentCustDistribution.setOnClickListener {
            val bottomSheet = AgentCustomerList.newInstance(object : AgentCustomerList.OnAgentCustSelectedListener {
                override fun onOnAgentCustSelected(model: AgentCustomerData) {
                    custId = model.ID
                    binding.textviewAgentCustDistributionCustomerName.text = model.NAME
                }
            })
            bottomSheet.show(supportFragmentManager, "bottomFragment")
        }

        binding.tvAgentCustDistributionTotalAmount.setOnClickListener {
            if (validate()) {
                commercialCylinderQyt = binding.editextAgentCustDistributionNoOfCommercialCylinders.text.toString().toInt()
                domesticCylinderQyt = binding.edittextAgentCustDistributionDomesticCylinder.text.toString().toInt()
                miniComCylinderQyt = binding.edittextAgentCustDistributionMiniCylinderCom.text.toString().toInt()
                miniDomCylinderQyt = binding.edittextAgentCustDistributionMiniCylinderDom.text.toString().toInt()
                for(item in cylindersDetails) {
                    Log.d("####", "setOnClickListener: ${item.PURCHASE_RATE} ${item.RATE}} $commercialCylinderQyt")
                    if (item.ID == 1) {
                        totAmount += item.PURCHASE_RATE * commercialCylinderQyt
                    } else if (item.ID == 2) {
                        totAmount += item.PURCHASE_RATE * domesticCylinderQyt
                    } else if (item.ID == 3) {
                        totAmount += item.PURCHASE_RATE * miniComCylinderQyt
                    } else {
                        totAmount += item.PURCHASE_RATE * miniDomCylinderQyt
                    }
                }
                binding.tvAgentCustDistributionTotalAmount.text = "Rs. ${totAmount.toString()}"
            }
        }

        binding.etAgentCustDistributionDate.apply {
            setOnClickListener {
                val datePicker = DatePickerDialog(this@AgentCustDistributionActivity, {_, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    //textDateFormat(calendar.time)
                    text = textDateFormat(calendar.time)
                    serverDate = dateFormat.format(calendar.time)
                },  calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.agentCustDistributionLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    showToast("Cylinder Distributed Successfully")
                    startActivity(Intent(this@AgentCustDistributionActivity, DashboardActivity::class.java))
                }
                is Response.Error -> {
                    progressDialog.dismiss()
                    showToast(response.exception.message.toString())
                }
                else -> return@observe
            }
        }
    }
    private fun validate() : Boolean {
        if (custId == -1) {
            showToast("Please select customer")
            return false
        }

        if (binding.editextAgentCustDistributionNoOfCommercialCylinders.text.isNullOrEmpty()) {
            showToast("Please enter 19 Kg cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustDistributionDomesticCylinder.text.isNullOrEmpty()) {
            showToast("Please enter 14.2 Kg cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustDistributionMiniCylinderCom.text.isNullOrEmpty()) {
            showToast("Please enter 5kg Com cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustDistributionMiniCylinderDom.text.isNullOrEmpty()) {
            showToast("Please enter  5kg Dom cylinder quantity")
            return false
        }

        if (binding.etAgentCustDistributionDate.text.equals("Select Date")) {
            showToast("Please select date")
            return false
        }

        if (totAmount == 0) {
            showToast("Please calculate total amount")
            return false
        }
        return true
    }

    private fun textDateFormat(date: Date): String {
        return dateFormat.format(date)
    }

    private fun showToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}