package com.ftg2024.ftgcylinderapp.agentdistributionmanagement

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustReturnRequest
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerData
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.OrderData
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModel
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModelFactory
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentCustDistributionBinding
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentCustReturnBinding
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
class AgentCustReturnActivity : AppCompatActivity() {

    @Inject
    lateinit var factory : AgentCustDistributionViewModelFactory

    @Inject
    lateinit var prefManager: SharedPrefManager

    private val viewModel : AgentCustDistributionViewModel by viewModels { factory }

    private lateinit var binding: ActivityAgentCustReturnBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var request : AgentCustReturnRequest
    private lateinit var agentCustDistributionList: MutableList<OrderData>
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

    private var totAmount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityAgentCustReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        agentId = prefManager.getLoginResponseData()!![0].UserData[0].USER_ID
        progressDialog = ProgressDialog(this,"Loading...")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setOnClickListener()
        setUpObservers()
    }

    private fun setOnClickListener() {
        binding.buttonAgentCustReturnSubmit.setOnClickListener {
            if (validate()) {
                commercialCylinderQyt = binding.editextAgentCustReturnNoOfCommercialCylinders.text.toString().toInt()
                domesticCylinderQyt = binding.edittextAgentCustReturnDomesticCylinder.text.toString().toInt()
                miniComCylinderQyt = binding.edittextAgentCustReturnMiniCylinderCom.text.toString().toInt()
                miniDomCylinderQyt = binding.edittextAgentCustReturnMiniCylinderDom.text.toString().toInt()
                agentCustDistributionList = mutableListOf()
                agentCustDistributionList.add(OrderData(1, commercialCylinderQyt))
                agentCustDistributionList.add(OrderData(2, domesticCylinderQyt))
                agentCustDistributionList.add(OrderData(3, miniComCylinderQyt))
                agentCustDistributionList.add(OrderData(4, miniDomCylinderQyt))
                progressDialog.show()
                request = AgentCustReturnRequest(agentId, custId, serverDate, agentCustDistributionList)
                viewModel.agentCustReturn(request)
            }
        }

        binding.searchAgentCustReturn.setOnClickListener {
            val bottomSheet = AgentCustomerList.newInstance(object : AgentCustomerList.OnAgentCustSelectedListener {
                override fun onOnAgentCustSelected(model: AgentCustomerData) {
                    custId = model.ID
                    binding.textviewAgentCustReturnCustName.text = model.NAME
                }
            })
            bottomSheet.show(supportFragmentManager, "bottomFragment")
        }

        binding.etAgentCustReturnDate.apply {
            setOnClickListener {
                val datePicker = DatePickerDialog(this@AgentCustReturnActivity, {_, year, month, dayOfMonth ->
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
        viewModel.agentCustReturnLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    showToast("Cylinder Returned Successfully")
                    startActivity(Intent(this@AgentCustReturnActivity, DashboardActivity::class.java))
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

        if (binding.editextAgentCustReturnNoOfCommercialCylinders.text.isNullOrEmpty()) {
            showToast("Please enter 19 Kg cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustReturnDomesticCylinder.text.isNullOrEmpty()) {
            showToast("Please enter 14.2 Kg cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustReturnMiniCylinderCom.text.isNullOrEmpty()) {
            showToast("Please enter 5kg Com cylinder quantity")
            return false
        }

        if (binding.edittextAgentCustReturnMiniCylinderDom.text.isNullOrEmpty()) {
            showToast("Please enter  5kg Dom cylinder quantity")
            return false
        }

        if (binding.etAgentCustReturnDate.text.equals("Select Date")) {
            showToast("Please select date")
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