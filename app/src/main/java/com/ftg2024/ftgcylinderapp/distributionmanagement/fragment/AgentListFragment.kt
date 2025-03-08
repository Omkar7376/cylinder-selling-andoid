package com.ftg2024.ftgcylinderapp.distributionmanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftg2024.ftgcylinderapp.databinding.FragmentAgentListBinding
import com.ftg2024.ftgcylinderapp.distributionmanagement.adapter.AgentListAdapter
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModel
import com.ftg2024.ftgcylinderapp.distributionmanagement.viewmodel.DistributionMgmtViewModelFactory
import com.ftg2024.ftgcylinderapp.uidata.Response
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AgentListFragment : BottomSheetDialogFragment(), AgentListAdapter.OnAgentCLickListener {

    @Inject
    lateinit var factory : DistributionMgmtViewModelFactory

    private val viewModel by viewModels<DistributionMgmtViewModel> { factory }

    private lateinit var binding : FragmentAgentListBinding
    private lateinit var onAgentCLickedListener : OnAgentSelectedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentAgentListBinding.inflate(layoutInflater)
        viewModel.getAgentList()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.agentDetailsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Success -> {
                    val agentListResponse = response.data
                    Log.d("####", "setupObservers: $agentListResponse")
                    if (agentListResponse!= null) {
                        if (agentListResponse.code == 200) {
                            val agentList = agentListResponse.data
                            if (!agentList.isNullOrEmpty()) {
                                val adapter = AgentListAdapter(agentList, this)
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
        fun newInstance(listener : OnAgentSelectedListener) =
            AgentListFragment().apply {
                onAgentCLickedListener = listener
            }
    }

    override fun onAgentCLicked(model: AgentData) {
        onAgentCLickedListener.onOnAgentSelected(model)
        dismiss()
    }


    interface OnAgentSelectedListener {
        fun onOnAgentSelected(model: AgentData)
    }
}

