package com.ftg2024.ftgcylinderapp.distributionmanagement.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.databinding.FragmentCylinederSelectionBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CylinederSelectionBottomFragment : BottomSheetDialogFragment() {
    private var cylinderType = 0
    private var cylinderAmount = 0

    private lateinit var binding : FragmentCylinederSelectionBottomBinding
    private lateinit var listener : OnSelectedCylinderDetailsSubmitListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCylinederSelectionBottomBinding.inflate(layoutInflater)
        setUp()
        binding.radioGroupOptions.setOnCheckedChangeListener { group, checkedId ->
            Log.d("####", "RadioGroup Options clicked: $checkedId")
            cylinderType = when (checkedId) {
                R.id.radio_option_1 -> 1
                R.id.radio_option_2 -> 2
                R.id.radio_option_3 -> 3
                else -> -1 // Default case
            }
            Log.d("####", "Selected Cylinder Type: $cylinderType")
        }
        binding.confirmButton.setOnClickListener {
            cylinderAmount = binding.cylinderQuantity.text.toString().toInt()
            Log.d("####", "onCreateView: $cylinderType $cylinderAmount")
            listener.selectedCylinderDetails(cylinderType, cylinderAmount)
            dismiss()
        }
        return binding.root
    }

    private fun setUp() {
        Log.d("####", "setUp: $cylinderAmount")
        if (cylinderAmount != -1) {
            binding.cylinderQuantity.setText(cylinderAmount.toString())
        }
        if (cylinderType != -1) {
            when(cylinderType) {
                1 -> binding.radioOption1.isChecked = true
                2 -> binding.radioOption2.isChecked = true
                3 -> binding.radioOption3.isChecked = true
            }
            disableRadioButtons(cylinderType)
        }
    }

    private fun disableRadioButtons(type : Int) {
        binding.radioOption1.isClickable = false
        binding.radioOption2.isClickable = false
        binding.radioOption3.isClickable = false
        if (type == 1) {
            binding.radioOption1.isClickable = true
        }
        if (type == 2) {
            binding.radioOption2.isClickable = true
        }
        if (type == 3) {
            binding.radioOption3.isClickable = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener : OnSelectedCylinderDetailsSubmitListener, cylinderType : Int, cylinderAmount : Int) =
            CylinederSelectionBottomFragment().apply {
               this.listener = listener
                this.cylinderAmount = cylinderAmount
                this.cylinderType = cylinderType
            }
    }

    interface OnSelectedCylinderDetailsSubmitListener{
        fun selectedCylinderDetails(type: Int, quantity: Int)
    }
}