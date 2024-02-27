package com.rn.giragrana.form
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository


class ProductFormViewModel(
    private val repository: ProductRepository
) : ViewModel(){
    private val validator by lazy{ProductValidator()}

    fun loadProduct(id: Long): LiveData<Product>{
        return repository.productById(id)
    }

    fun saveProduct(product: Product): Boolean{
        return validator.validate(product)
            .also{validated ->
                if(validated) repository.save(product)
            }
    }
}