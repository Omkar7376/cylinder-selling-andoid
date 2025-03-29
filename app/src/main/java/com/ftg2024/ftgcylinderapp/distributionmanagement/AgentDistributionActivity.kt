package com.ftg2024.ftgcylinderapp.distributionmanagement

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.MyApp
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentDistributionBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.AgentListFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.CylinederSelectionBottomFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentDistributionRequest
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.DistributionData
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AgentDistributionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentDistributionBinding
    private var isReturn = false
    private val app: MyApp by lazy { application as MyApp }
    private val calendar = Calendar.getInstance()
    private var id = -1
    private var serverDate : String? = null
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private var domesticCylinderQyt = 0

    private var commercialCylinderQyt = 0

    private var miniDomCylinderQyt = 0

    private var miniComCylinderQyt = 0

    private lateinit var request : AgentDistributionRequest
    private lateinit var progressDialog : ProgressDialog
    @Inject
    lateinit var factory : DistributionMgmtViewModelFactory

    private val viewmodel : DistributionMgmtViewModel by viewModels{ factory }

    private lateinit var cylindersDetails : List<CylinderDetailsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityAgentDistributionBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog(this, "Loading...")
        getBundleValues()
        setContentView(binding.root)
        setOnClickListeners()
        cylindersDetails = app.cylinderDetailsData
        Log.d("####", "onCreate: ${app.cylinderDetailsData}")
        request = intent.getParcelableExtra("distribution_request") ?: return
        isReturn = intent.getBooleanExtra("is_return", false)
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isReturn = bundle?.getBoolean("is_return") ?: false
        Log.d("####", "getBundleValues: $isReturn")
    }

    private fun setOnClickListeners() {
        binding.searchAgentDistribution.setOnClickListener {
            val bottomSheet = AgentListFragment.newInstance(object : AgentListFragment.OnAgentSelectedListener {
                override fun onOnAgentSelected(model: AgentData) {
                    id = model.ID
                    binding.textviewAgentDistributionAgentName.text = model.NAME
                    binding.textviewAgentDistributionAgentMobno.text = model.MOBILE_NO
                }
            })
            bottomSheet.show(supportFragmentManager, "AgentListFragment")
        }

        binding.etAgentDistriDate.apply {
            setOnClickListener {
                val datePicker = DatePickerDialog(this@AgentDistributionActivity, {_, year, month, dayOfMonth ->
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

        binding.buttonAgentDistributionSubmit.setOnClickListener {
            if (validate()) {
                serverDate = binding.etAgentDistriDate.text.toString()
                commercialCylinderQyt = binding.editextNoOfCommercialCylinders.text.toString().ifEmpty { "0" }.toInt()
                domesticCylinderQyt = binding.edittextAgentDistributionDomesticCylinder.text.toString().ifEmpty { "0" }.toInt()
                miniComCylinderQyt = binding.edittextAgentDistributionMiniCylinderCom.text.toString().ifEmpty { "0" }.toInt()
                miniDomCylinderQyt = binding.edittextAgentDistributionMiniCylinderDom.text.toString().ifEmpty { "0" }.toInt()

                val requestList = mutableListOf<DistributionData>()
                for (details in cylindersDetails) {
                    if (details.ID == 1) {
                        requestList.add(DistributionData((details.PURCHASE_RATE*commercialCylinderQyt),1, commercialCylinderQyt, serverDate!!))
                    } else if (details.ID == 2) {
                        requestList.add(DistributionData((details.PURCHASE_RATE*domesticCylinderQyt),2, domesticCylinderQyt, serverDate!!))
                    } else if(details.ID == 3) {
                        requestList.add(DistributionData((details.PURCHASE_RATE*miniComCylinderQyt),3, miniComCylinderQyt, serverDate!!))
                    } else {
                        requestList.add(DistributionData((details.PURCHASE_RATE*miniDomCylinderQyt),4, miniDomCylinderQyt, serverDate!!))
                    }
                }
                request = AgentDistributionRequest(id, requestList, serverDate!!)
                Log.d("####", "setOnClickListeners: $request")
                progressDialog.dismiss()
                showDistributionConfirmation()
            } else {
                Log.d("####", "Validation failed")
            }
        }
    }

    /*private fun setupObservers() {
        viewmodel.distributionMutableLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Cylinder Distributed Successfully", Toast.LENGTH_SHORT).show()
                }
                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
                else -> return@observe
            }
        }
    }*/

    private fun textDateFormat(date: Date): String {
        return dateFormat.format(date)
    }

    private fun validate() : Boolean{
        if (id == -1){
            return false
        }
        if (binding.etAgentDistriDate.text == "Select Date")
            return false
        if (binding.editextNoOfCommercialCylinders.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentDistributionDomesticCylinder.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentDistributionMiniCylinderCom.text.isNullOrEmpty())
            return false
        if (binding.edittextAgentDistributionMiniCylinderDom.text.isNullOrEmpty())
            return false
        return true
    }

    private fun showDistributionConfirmation() {
        Log.d("####", "Navigating to confirmation screen")
        val gson = Gson()
        val distributionRequestJson = gson.toJson(request)

        val intent = Intent(this, AgentDistributionConfirmationActivity::class.java).apply{
            putExtra("distribution_request_json", distributionRequestJson)
            putExtra("is_return", isReturn)
            putExtra("Delivery_Boy_Name", binding.textviewAgentDistributionAgentName.text)
        }
        try {
            startActivity(intent)
            Log.d("####", "Activity started successfully")
        } catch (e: Exception) {
            Log.e("####", "Error starting activity", e)
        }
    }

    /* private fun setOnClickListeners() {
         binding.textviewUpdateSearchAgent.setOnClickListener {
             val bottomSheet = AgentListFragment.newInstance(object : AgentListFragment.OnAgentSelectedListener {
                 override fun onOnAgentSelected(model: AgentData) {
                     binding.textviewUpdateSearchAgent.text = model.NAME
                     binding.tvNameValue.text = model.NAME
                     binding.tvPhoneNumberValue.text = model.MOBILE_NO
                 }
             })
             bottomSheet.show(supportFragmentManager, "AgentListFragment")
         }

         binding.tvAddLink.setOnClickListener {
             showCylinederSelectionBottomFragment()
         }

         binding.labelAgentDistributionCommercialEdit.setOnClickListener {
             cylinderType = 1
             cylinderQuantity = commercialCylinderQuantity
             showCylinederSelectionBottomFragment()
         }

         binding.labelAgentDistributionDomesticEdit.setOnClickListener {
             cylinderType = 2
             cylinderQuantity = domesticCylinderQuantity
             showCylinederSelectionBottomFragment()
         }

         binding.labelAgentDistributionMiniEdit.setOnClickListener {
             cylinderType = 3
             cylinderQuantity = miniCylinderQuantity
             showCylinederSelectionBottomFragment()
         }

         binding.labelAgentDistributionCommercialRemove.setOnClickListener {
             commercialCylinderQuantity = 0
             binding.cardAgentDistributionCommercialCylinders.visibility = View.GONE
         }

         binding.labelAgentDistributionDomesticRemove.setOnClickListener {
             domesticCylinderQuantity = 0
             binding.cardAgentDistributionDomesticCylinders.visibility = View.GONE
         }
         binding.labelAgentDistributionMiniRemove.setOnClickListener {
             miniCylinderQuantity = 0
             binding.cardAgentDistributionMiniCylinders.visibility = View.GONE
         }
     }

     private fun showCylinederSelectionBottomFragment() {
         val bottomSheet = CylinederSelectionBottomFragment.newInstance(object : CylinederSelectionBottomFragment.OnSelectedCylinderDetailsSubmitListener {
             override fun selectedCylinderDetails(type: Int, quantity: Int) {
                 cylinderType = type
                 if (cylinderType == 1) {
                     commercialCylinderQuantity = quantity
                     binding.textviewAgentDistributionCommercialQuantity.text = commercialCylinderQuantity.toString()
                     binding.cardAgentDistributionCommercialCylinders.visibility = View.VISIBLE
                 } else if(cylinderType == 2) {
                     domesticCylinderQuantity = quantity
                     binding.textviewAgentDistributionDomesticQuantity.text = domesticCylinderQuantity.toString()
                     binding.cardAgentDistributionDomesticCylinders.visibility = View.VISIBLE
                 } else {
                     miniCylinderQuantity = quantity
                     binding.textviewAgentDistributionMiniQuantity.text = miniCylinderQuantity.toString()
                     binding.cardAgentDistributionMiniCylinders.visibility = View.VISIBLE
                 }
                 cylinderType = -1
                 Log.d("####", "selectedCylinderDetails: $commercialCylinderQuantity $domesticCylinderQuantity $miniCylinderQuantity")
             }
         }, cylinderType, cylinderQuantity)
         bottomSheet.show(supportFragmentManager, "CylinederSelectionBottomFragment")
     }*/
}