package com.rnoronha.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rnoronha.giragrana.databinding.FragmentTotalResalesBinding
import com.rnoronha.giragrana.model.Resale
import com.rnoronha.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
class TotalResalesFragment : Fragment() {

    private lateinit var binding: FragmentTotalResalesBinding
    private val viewModel: ResaleListViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalResalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getResales()?.observe(viewLifecycleOwner, Observer { resales ->
            binding.txtTotalResales.text = calculateTotalResales(resales)
        })

    }


    private fun calculateTotalResales(resales:List<Resale>):String{
        val totalResales = resales.sumByDouble { it.resalePrice.toDouble() }
        return PriceUtils.formatPrice(totalResales.toFloat())
    }


}