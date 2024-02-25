package com.rn.giragrana.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.rn.giragrana.list.ProductListFragmentDirections
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository

class ProductDetailsViewModel(
    private val repository: ProductRepository
) : ViewModel(){
    fun loadProductDetails(id: Long): LiveData<Product>{
        return repository.productById(id)
    }

    private val _navigateToProductForm = MutableLiveData<NavDirections?>()
    val navigateToProductForm = MutableLiveData<NavDirections?>()

    fun navigateToProductForm(productId: Long){
        val action = ProductDetailsFragmentDirections
            .actionProductDetailsFragmentToProductFormFragment(productId)
        _navigateToProductForm.value = action
    }

    fun onProductDetailsNavigated() {
        _navigateToProductForm.value = null
    }
}