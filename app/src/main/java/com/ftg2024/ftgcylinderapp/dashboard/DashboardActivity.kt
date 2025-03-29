package com.ftg2024.ftgcylinderapp.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ftg2024.ftgcylinderapp.MyApp
import com.ftg2024.ftgcylinderapp.admincustomermanagement.CustomerManagementActivity
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.AgentCustDistributionActivity
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.AgentCustReturnActivity
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponseData
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsData
import com.ftg2024.ftgcylinderapp.dashboard.model.CylinderDetailsResponse
import com.ftg2024.ftgcylinderapp.dashboard.viewmodel.DashBoardViewModelFactory
import com.ftg2024.ftgcylinderapp.dashboard.viewmodel.DashboardViewmodel
import com.ftg2024.ftgcylinderapp.databinding.ActivityDashboardBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.AgentDistributionActivity
import com.ftg2024.ftgcylinderapp.distributionmanagement.AgentReturnActivity
import com.ftg2024.ftgcylinderapp.distributionmanagement.DistributionSummaryActivity
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.sharedprefs.SharedPrefManager
import com.ftg2024.ftgcylinderapp.stockmanagement.StockCountActivity
import com.ftg2024.ftgcylinderapp.stockmanagement.UpdateStockActivity
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockUpdateRequest
import com.ftg2024.ftgcylinderapp.uidata.CylinderDetailsObject
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var loginResponse: List<LoginResponseData>

    private val app: MyApp by lazy { application as MyApp }

    @Inject
    lateinit var factory: DashBoardViewModelFactory

    @Inject
    lateinit var prefManager : SharedPrefManager

    private val viewModel: DashboardViewmodel by viewModels {factory}
    private var isAdmin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Fetching Data...")
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        loginResponse = prefManager.getLoginResponseData()!!
        setup()
        progressDialog.show()
        viewModel.getCylinderDetails()
        setUpObservers()
        getBundleValues()
        setOnClickListeners()
    }

    private fun setUpObservers() {
        viewModel.cylinderDetailsLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    val cylinderDetailsResponse = response.data
                    if (!cylinderDetailsResponse.data.isNullOrEmpty()) {
                        app.cylinderDetailsData = cylinderDetailsResponse.data.toMutableList()
                        setUpValues()
                    }
                }
                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this@DashboardActivity, "Failed to Fetch Details", Toast.LENGTH_SHORT).show()
                }

                Response.Loading -> return@observe
            }
        }
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isAdmin = bundle?.getBoolean("is_admin") ?: true
        Log.d("####", "getBundleValues: isAdmin = $isAdmin")
    }

    private fun setup() {
        binding.textviewDashboardUserName.text = loginResponse[0].UserData[0].NAME
        binding.textviewDashboardUserRole.text = if (loginResponse[0].UserData[0].ROLE == "U") {
            isAdmin = true
            "Admin"
        }  else {
            isAdmin = false
            "Delivery Boy"
        }

        if (isAdmin) {
            binding.containerDashboardTop.visibility = android.view.View.VISIBLE
            binding.containerDashboardAdmin.visibility = android.view.View.VISIBLE
            binding.containerDashboardAgent.visibility = android.view.View.GONE
            binding.scrollviewDashboardAdmin.visibility = android.view.View.VISIBLE
            binding.scrollviewDashboardAgent.visibility = android.view.View.GONE
        } else {
            binding.containerDashboardTop.visibility = android.view.View.VISIBLE
            binding.containerDashboardAdmin.visibility = android.view.View.GONE
            binding.containerDashboardAgent.visibility = android.view.View.VISIBLE
            binding.scrollviewDashboardAdmin.visibility = android.view.View.GONE
            binding.scrollviewDashboardAgent.visibility = android.view.View.VISIBLE
        }
    }

    private fun setUpValues() {
        app.cylinderDetailsData.forEach { stock ->
            when (stock.ID) {
                1 -> {
                    binding.textviewActivityDashboardAdmin19kgFull.text = stock.STOCK_FILLED.toString()
                    binding.textviewActivityDashboardAdmin19kgEmpty.text = stock.STOCK_EMPTY.toString()
                    binding.textviewActivityDashboardAdmin19kgDefective.text = stock.STOCK_DEFECTIVE.toString()
                }
                2 -> {
                    binding.textviewActivityDashboardAdmin142kgFull.text = stock.STOCK_FILLED.toString()
                    binding.textviewActivityDashboardAdmin142kgEmpty.text = stock.STOCK_EMPTY.toString()
                    binding.textviewActivityDashboardAdmin142kgDefective.text = stock.STOCK_DEFECTIVE.toString()
                }
                3 -> {
                    binding.textviewActivityDashboardAdmin5kgComFull.text = stock.STOCK_FILLED.toString()
                    binding.textviewActivityDashboardAdmin5kgComEmpty.text = stock.STOCK_EMPTY.toString()
                    binding.textviewActivityDashboardAdmin5kgComDefective.text = stock.STOCK_DEFECTIVE.toString()
                }
                4 -> {
                    binding.textviewActivityDashboardAdmin5kgDomFull.text = stock.STOCK_FILLED.toString()
                    binding.textviewActivityDashboardAdmin5kgDomEmpty.text = stock.STOCK_EMPTY.toString()
                    binding.textviewActivityDashboardAdmin5kgDomDefective.text = stock.STOCK_DEFECTIVE.toString()
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.imageCardDashboardStockManagementAdd.setOnClickListener {
            val intent = Intent(this, UpdateStockActivity::class.java)
            intent.putExtra("is_return", false)
            startActivity(intent)
        }

        binding.imageCardDashboardStockManagementReturn.setOnClickListener {
            val intent = Intent(this, UpdateStockActivity::class.java)
            intent.putExtra("is_return", true)
            startActivity(intent)
        }

        binding.imageCardDashboardStockManagementSummary.setOnClickListener {
            val intent = Intent(this, StockCountActivity::class.java)
            startActivity(intent)
        }

        binding.labelDashboardShowDistributionSummary.setOnClickListener {

        }

        binding.imageCardDashboardDistributionManagementAdd.setOnClickListener {
            val intent = Intent(this, AgentDistributionActivity::class.java)
            startActivity(intent)
        }

        binding.imageCardDashboardAgentDistributionManagementAdd.setOnClickListener {
            val intent = Intent(this, AgentCustDistributionActivity::class.java)
            startActivity(intent)
        }

        binding.imageCardDashboardAgentDistributionManagementReturn.setOnClickListener {
            val intent = Intent(this, AgentCustReturnActivity::class.java)
            startActivity(intent)
        }

       /* binding.imageCardDashboardPaymentManagementAdd.setOnClickListener {
            val intent = Intent(this, PaymentPendingActivity::class.java)
            startActivity(intent)
        }*/

        binding.imageCardDashboardDistributionManagementReturn.setOnClickListener {
            val intent = Intent(this, AgentReturnActivity::class.java)
            startActivity(intent)
        }

        binding.imageCardDashboardDistributionManagementSummary.setOnClickListener {
            val intent = Intent(this, DistributionSummaryActivity::class.java)
            startActivity(intent)
        }

        binding.imageCardDashboardAdminCustomerManagement.setOnClickListener{
            val intent = Intent(this, CustomerManagementActivity::class.java)
            startActivity(intent)
        }
    }

}