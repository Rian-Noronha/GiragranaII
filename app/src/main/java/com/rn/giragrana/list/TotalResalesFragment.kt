package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rn.giragrana.databinding.FragmentTotalResalesBinding
import com.rn.giragrana.model.Resale
import com.rn.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TotalResalesFragment : Fragment() {

    private lateinit var binding: FragmentTotalResalesBinding
    private val viewModel: ResaleListViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTotalResalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getResales().observe(viewLifecycleOwner) { resales ->
            binding.txtTotalResales.text = calculateTotalResales(resales)
        }

    }


    private fun calculateTotalResales(resales:List<Resale>):String{
        val totalResales = resales.sumOf { it.resalePrice.toDouble() }
        return PriceUtils.formatPrice(totalResales.toFloat())
    }


}