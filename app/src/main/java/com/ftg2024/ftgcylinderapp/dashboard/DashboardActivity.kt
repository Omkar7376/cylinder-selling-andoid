package com.ftg2024.ftgcylinderapp.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityDashboardBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.AgentDistributionActivity
import com.ftg2024.ftgcylinderapp.stockmanagement.StockCountActivity
import com.ftg2024.ftgcylinderapp.stockmanagement.UpdateStockActivity
import com.ftg2024.ftgcylinderapp.ui.AddNewStockActivity
import com.ftg2024.ftgcylinderapp.ui.DistributeCylinderActivity
import com.ftg2024.ftgcylinderapp.ui.ReturnCylinderActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private var isAdmin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        getBundleValues()
        setup()
        setOnClickListeners()
    }

    private fun getBundleValues() {
        val bundle = intent.extras
        isAdmin = bundle?.getBoolean("is_admin") ?: true
        Log.d("####", "getBundleValues: isAdmin = $isAdmin")
    }

    private fun setup() {
        if (isAdmin) {
            binding.containerDashboardTop.visibility = android.view.View.VISIBLE
            binding.containerDashboardAdmin.visibility = android.view.View.VISIBLE
            binding.containerDashboardAgent.visibility = android.view.View.GONE
        } else {
            binding.containerDashboardTop.visibility = android.view.View.VISIBLE
            binding.containerDashboardAdmin.visibility = android.view.View.GONE
            binding.containerDashboardAgent.visibility = android.view.View.VISIBLE
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
    }

}