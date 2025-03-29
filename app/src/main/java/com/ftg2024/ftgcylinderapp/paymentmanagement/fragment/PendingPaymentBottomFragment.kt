package com.ftg2024.ftgcylinderapp.distributionmanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftg2024.ftgcylinderapp.databinding.FragmentPendingPaymentBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PendingPaymentBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPendingPaymentBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPendingPaymentBottomBinding.inflate(inflater, container, false)

        binding.btnConfirmPayment.setOnClickListener {
            val amount = binding.edittextPendingPaymentAmount.text.toString()
            if (amount.isNotEmpty()) {
                dismiss()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PendingPaymentBottomFragment()
    }
}