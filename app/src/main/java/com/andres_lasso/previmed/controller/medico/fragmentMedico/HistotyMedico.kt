package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.medico.VisitsHistoryProvider
import com.andres_lasso.previmed.controller.medico.adapter.VisitsHistoryAdapter
import com.andres_lasso.previmed.databinding.FragmentHistotyMedicoBinding

class HistotyMedico : Fragment() {

    private var _binding: FragmentHistotyMedicoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistotyMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = binding.listHistory
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = VisitsHistoryAdapter(VisitsHistoryProvider.visitsHistoryList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
