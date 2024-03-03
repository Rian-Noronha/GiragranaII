package com.rn.giragrana.repository

import androidx.lifecycle.LiveData
import com.rn.giragrana.model.Product

interface ProductRepository {
    fun save(product: Product)
    fun remove(vararg products: Product)
    fun productById(id:Long): LiveData<Product>
    fun search(term: String): LiveData<List<Product>>

    fun getProductsMap(): LiveData<Map<Long, Product>>
}