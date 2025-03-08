package com.ftg2024.ftgcylinderapp.distributionmanagement

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentDistributionBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.AgentListFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.CylinederSelectionBottomFragment
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgentDistributionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentDistributionBinding

    private var cylinderType = ""
    private var cylinderQuantity = ""
    private var agentName = ""
    private var agentMobileNo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityAgentDistributionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
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
            val bottomSheet = CylinederSelectionBottomFragment.newInstance("","")
            bottomSheet.show(supportFragmentManager, "CylinederSelectionBottomFragment")
        }
    }
}