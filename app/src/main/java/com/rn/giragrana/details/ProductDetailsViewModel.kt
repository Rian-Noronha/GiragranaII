package com.rn.giragrana.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository

class ProductDetailsViewModel(
    private val repository: ProductRepository
) : ViewModel(){
    fun loadProductDetails(id: Long): LiveData<Product>{
        return repository.productById(id)
    }
}