package com.rn.giragrana.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository

class ProductDetailsViewModel(
    private val repository: ProductRepository
) : ViewModel(){
    fun loadProductDetails(id: Long): LiveData<Product>{
        return repository.productById(id)
    }
}