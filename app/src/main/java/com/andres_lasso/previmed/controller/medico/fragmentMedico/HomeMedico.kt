package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [HomeMedico.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeMedico : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_medico, container, false)

    }
}