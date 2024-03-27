package com.rnoronha.giragrana.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rnoronha.giragrana.model.Product
import com.rnoronha.giragrana.repository.ProductRepository

class ProductDetailsViewModel(
    private val repository: ProductRepository
) : ViewModel(){
    fun loadProductDetails(id: Long): LiveData<Product>{
        return repository.productById(id)
    }
}