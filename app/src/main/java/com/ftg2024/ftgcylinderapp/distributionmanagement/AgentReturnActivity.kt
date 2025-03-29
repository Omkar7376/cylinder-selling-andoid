package com.ftg2024.ftgcylinderapp.distributionmanagement

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
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentDistributionBinding
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentReturnBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.AgentListFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentReturnCylinderRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionData
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.ReturnData
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AgentReturnActivity : AppCompatActivity() {
    private var totAmount: Int = 0
    private lateinit var binding: ActivityAgentReturnBinding
    private lateinit var progressDialog : ProgressDialog
    private val app: MyApp by lazy { application as MyApp }
    private val calendar = Calendar.getInstance()
    private var id = -1
    private var serverDate : String? = null
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private var domesticCylinderQyt = 0

    private var commercialCylinderQyt = 0

    private var miniDomCylinderQyt = 0

    private var miniComCylinderQyt = 0

    private lateinit var request : AgentReturnCylinderRequest

    private var requestList = mutableListOf<ReturnData>()
    private lateinit var cylindersDetails : List<CylinderDetailsData>

    @Inject
    lateinit var factory: DistributionMgmtViewModelFactory

    private val viewmodel : DistributionMgmtViewModel by viewModels<DistributionMgmtViewModel> { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding =  ActivityAgentReturnBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog(this,"Loading...")
        setContentView(binding.root)
        cylindersDetails = app.cylinderDetailsData
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setOnClickListeners()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewmodel.returnMutableLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Cylinders Returned Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                else -> return@observe
            }
        }
    }

    private fun setOnClickListeners() {
        binding.searchAgentReturn.setOnClickListener {
            val bottomSheet = AgentListFragment.newInstance(object : AgentListFragment.OnAgentSelectedListener {
                override fun onOnAgentSelected(model: AgentData) {
                    id = model.ID
                    binding.textviewAgentReturnAgentName.text = model.NAME
                    binding.textviewAgentReturnAgentMobno.text = model.MOBILE_NO
                }
            })
            bottomSheet.show(supportFragmentManager, "AgentListFragment")
        }

        binding.etAgentReturnDate.apply {
            setOnClickListener {
                val datePicker = DatePickerDialog(this@AgentReturnActivity, {_, year, month, dayOfMonth ->
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

        binding.tvReturnTotalAmount.setOnClickListener {
            Log.d("####", "setOnClickListeners: $cylindersDetails")
            if (validate()) {
                commercialCylinderQyt = binding.editextNoOfCommercialCylinders.text.toString().toInt()
                domesticCylinderQyt = binding.edittextAgentReturnDomesticCylinder.text.toString().toInt()
                miniComCylinderQyt = binding.edittextAgentReturnMiniCylinderCom.text.toString().toInt()
                miniDomCylinderQyt = binding.edittextAgentReturnMiniCylinderDom.text.toString().toInt()
                totAmount = 0
                for (details in cylindersDetails) {
                    if (details.ID == 1) {
                        Log.d("####", "setOnClickListeners: ${details.PURCHASE_RATE*commercialCylinderQyt}")
                        totAmount += details.PURCHASE_RATE*commercialCylinderQyt
                        requestList.add(ReturnData(1, commercialCylinderQyt))
                    } else if (details.ID == 2) {
                        Log.d("####", "setOnClickListeners: ${details.PURCHASE_RATE*domesticCylinderQyt}")
                        totAmount += details.PURCHASE_RATE*domesticCylinderQyt
                        requestList.add(ReturnData(2, domesticCylinderQyt))
                    } else if(details.ID == 3) {
                        Log.d("####", "setOnClickListeners: ${details.PURCHASE_RATE*domesticCylinderQyt}")
                        totAmount += details.PURCHASE_RATE*miniComCylinderQyt
                        requestList.add(ReturnData(3, miniComCylinderQyt))
                    } else if (details.ID == 4){
                        Log.d("####", "setOnClickListeners: ${details.PURCHASE_RATE*domesticCylinderQyt}")
                        totAmount += details.PURCHASE_RATE*miniDomCylinderQyt
                        requestList.add(ReturnData(4, miniDomCylinderQyt))
                    } else {

                    }
                }
                binding.tvReturnTotalAmount.text = "Rs. ${totAmount.toString()}"
            }
        }

        binding.buttonAgentDistributionSubmit.setOnClickListener {
            if (validate()) {
                serverDate = binding.etAgentReturnDate.text.toString()
                request = AgentReturnCylinderRequest(id, serverDate!!, totAmount, requestList)
                progressDialog.show()
                viewmodel.returnCylinder(request)
            }
        }
    }

    private fun textDateFormat(date: Date): String {
        return dateFormat.format(date)
    }

    private fun validate() : Boolean{
        if (id == -1) {
            return false
        }
        if (binding.etAgentReturnDate.text == "Select Date")
            return false
        if (binding.editextNoOfCommercialCylinders.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentReturnDomesticCylinder.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentReturnMiniCylinderCom.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentReturnMiniCylinderDom.text.isNullOrEmpty())
            return false
        return true
    }
}