package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.PagosAdd
import com.andres_lasso.previmed.controller.asesor.recycler.PagosProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PagosAdapter
import com.andres_lasso.previmed.databinding.FragmentPagosAsesorBinding

class PagosAsesorFragment : Fragment() {
    private var _binding: FragmentPagosAsesorBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PagosAdapter
    private val listaOriginal = PagosProvider.pagosList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPagosAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerPagosAsesor
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PagosAdapter(listaOriginal)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        super.onViewCreated(view, savedInstanceState)
        val  colorLetra = ContextCompat.getColor(requireContext(), R.color.AzulOscuro_Prevemed)
        val buscador = binding.buscarPago.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        buscador?.setTextColor(colorLetra)

        binding.btnIrPagosAdd.setOnClickListener {
            val irPagosAdd = Intent(requireContext(), PagosAdd::class.java)
            startActivity(irPagosAdd)
        }

        binding.buscarPago.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val listaFiltrada = listaOriginal.filter {
                    it.titular.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updatePagos(listaFiltrada)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
