package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.adapter.AgentCustomerListAdapter
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerData
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModel
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.viewmodel.AgentCustDistributionViewModelFactory
import com.ftg2024.ftgcylinderapp.databinding.FragmentAgentCustomerListBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AgentCustomerList : BottomSheetDialogFragment(), AgentCustomerListAdapter.OnAgentCustClickListener {

    @Inject
    lateinit var factory: AgentCustDistributionViewModelFactory

    private val viewModel by viewModels<AgentCustDistributionViewModel>{ factory }

    private lateinit var binding: FragmentAgentCustomerListBinding

    private lateinit var onAgentCustCLickedListener : OnAgentCustSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAgentCustomerListBinding.inflate(layoutInflater)
        viewModel.getAgentCustList()
        setUpObservers()
        return binding.root
    }

    private fun setUpObservers() {
        viewModel.agentCustDetailsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Success -> {
                    val agentCustomerListResponse = response.data
                    Log.d("####", "setupObservers: $agentCustomerListResponse")
                    if (agentCustomerListResponse!= null) {
                        if (agentCustomerListResponse.code == 200) {
                            val agentCustomerList = agentCustomerListResponse.data
                            if (!agentCustomerList.isNullOrEmpty()) {
                                val adapter = AgentCustomerListAdapter(agentCustomerList, this)
                                binding.recyclerviewAgentList.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
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
                    val error = response.exception
                    showToast("Something Went Wrong")
                }

                else -> return@observe
            }
        }
    }

    private fun showToast(msg: String) {

    }

    companion object {
        @JvmStatic
        fun newInstance(listener : OnAgentCustSelectedListener) =
            AgentCustomerList().apply {
                onAgentCustCLickedListener = listener
            }
    }

    override fun onAgentCustCLicked(model: AgentCustomerData) {
        onAgentCustCLickedListener.onOnAgentCustSelected(model)
        dismiss()
    }

    interface OnAgentCustSelectedListener {
        fun onOnAgentCustSelected(model: AgentCustomerData)
    }

}