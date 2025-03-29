package com.ftg2024.ftgcylinderapp.paymentmanagement

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.ActivityPaymentPendingBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.fragment.PendingPaymentBottomFragment

class PaymentPendingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentPendingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentPendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textviewClearPaymentLink.setOnClickListener {
            val bottomSheet = PendingPaymentBottomFragment()
            bottomSheet.show(supportFragmentManager, "PendingPaymentBottomFragment")
        }
    }
}
