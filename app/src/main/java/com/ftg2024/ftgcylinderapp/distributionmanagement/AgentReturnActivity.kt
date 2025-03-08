package com.ftg2024.ftgcylinderapp.distributionmanagement

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityAgentReturnBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import javax.inject.Inject

class AgentReturnActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAgentReturnBinding

    @Inject
    lateinit var factory : DistributionMgmtViewModelFactory

    private val viewmodel : DistributionMgmtViewModel by viewModels<DistributionMgmtViewModel> { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding =  ActivityAgentReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setOnCLickListener() {

    }
}