package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rn.giragrana.databinding.FragmentTotalProfitBinding
import com.rn.giragrana.model.Product
import com.rn.giragrana.model.Resale
import com.rn.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
class TotalProfitFrament : Fragment(){
    private lateinit var binding: FragmentTotalProfitBinding
    private val viewModelProduct: ProductListViewModel by sharedViewModel()
    private val viewModelResale: ResaleListViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentTotalProfitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelProduct.getProducts()?.observe(viewLifecycleOwner, Observer { products ->
            viewModelResale.getResales()?.value?.let { resales ->
                updateTotalProfit(products, resales)
            }
        })

        viewModelResale.getResales()?.observe(viewLifecycleOwner, Observer { resales ->
            viewModelProduct.getProducts()?.value?.let { products ->
                updateTotalProfit(products, resales)
            }
        })
    }

    private fun updateTotalProfit(products: List<Product>, resales: List<Resale>) {
        val totalProfit = calculateSumOfResales(resales) - calculateSumOfProducts(products)
        if(totalProfit > 0){
            binding.txtTotalProfit.text = PriceUtils.formatPrice(totalProfit)
        }else{
            binding.txtTotalProfit.text = PriceUtils.formatPrice(0f)
        }
    }

    private fun calculateSumOfResales(resales: List<Resale>): Float {
        return resales.sumByDouble { it.resalePrice.toDouble() }.toFloat()
    }

    private fun calculateSumOfProducts(products: List<Product>): Float {
        return products.sumByDouble { it.price.toDouble() }.toFloat()
    }



}