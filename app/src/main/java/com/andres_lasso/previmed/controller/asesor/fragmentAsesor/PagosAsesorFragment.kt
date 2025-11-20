package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.asesor.recycler.PagosProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PagosAdapter
import com.andres_lasso.previmed.databinding.FragmentPagosAsesorBinding
import com.andres_lasso.previmed.view.pagos.PagosAdd
import androidx.core.widget.addTextChangedListener

class PagosAsesorFragment : Fragment() {

    private var _binding: FragmentPagosAsesorBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PagosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagosAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerPagosAsesor.layoutManager = LinearLayoutManager(context)
        adapter = PagosAdapter(PagosProvider.pagosList)
        binding.recyclerPagosAsesor.adapter = adapter
    }

    private fun initSearch() {
        binding.etBuscarPago.addTextChangedListener { text ->
            adapter.filter(text.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearch()

        binding.btnIrPagosAdd.setOnClickListener {
            val irPagosAdd = Intent(requireContext(), PagosAdd::class.java)
            startActivity(irPagosAdd)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
