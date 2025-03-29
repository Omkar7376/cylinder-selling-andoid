package com.ftg2024.ftgcylinderapp.distributionmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityDistributionSummaryBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.adapter.AgentListAdapter
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DistributionSummaryActivity : AppCompatActivity(), AgentListAdapter.OnAgentCLickListener {
    private lateinit var binding : ActivityDistributionSummaryBinding
    private lateinit var progressDialog: ProgressDialog
    @Inject
    lateinit var factory : DistributionMgmtViewModelFactory

    private val viewModel by viewModels<DistributionMgmtViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDistributionSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, "Fetching Data...")
        progressDialog.show()
        viewModel.getAgentList()
        binding.cardDistriSummaryTemp.setOnClickListener {
            val intent = Intent(this, DistributionDetailsActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.agentDetailsMutableLiveData.observe(this) { response ->
            when(response) {
                is Response.Success -> {
                    progressDialog.dismiss()
                    val agentListResponse = response.data
                    Log.d("####", "setupObservers: $agentListResponse")
                    if (agentListResponse!= null) {
                        if (agentListResponse.code == 200) {
                            val agentList = agentListResponse.data
                            if (!agentList.isNullOrEmpty()) {
                                val adapter = AgentListAdapter(agentList, this)
                                binding.recyclerViewDistributionSummaryAgent.apply {
                                    layoutManager = LinearLayoutManager(this@DistributionSummaryActivity)
                                    this.adapter = adapter
                                }
                            } else {
                                showToast("Nothing To Show")
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
                    val error = response.exception
                    showToast("Something Went Wrong")
                }

                else -> return@observe
            }
        }
    }
    private fun showToast(msg: String) {

    }

    override fun onAgentCLicked(model: AgentData) {
        val intent = Intent(this, DistributionDetailsActivity::class.java)
        intent.putExtra("agent_id", model.ID)
        startActivity(intent)
    }
}