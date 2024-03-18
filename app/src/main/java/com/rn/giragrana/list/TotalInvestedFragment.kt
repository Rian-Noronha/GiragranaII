package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rn.giragrana.databinding.FragmentTotalInvestedBinding
import com.rn.giragrana.model.Product
import com.rn.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
class TotalInvestedFragment : Fragment() {
    private lateinit var binding: FragmentTotalInvestedBinding
    private val viewModel: ProductListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalInvestedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts()?.observe(viewLifecycleOwner, Observer { products ->
            binding.txtTotalInvested.text = calculateTotalInvested(products)
        })
    }


    private fun calculateTotalInvested(products: List<Product>): String {
        val totalInvested = products.sumByDouble { it.price.toDouble() }
        return PriceUtils.formatPrice(totalInvested.toFloat())
    }




}