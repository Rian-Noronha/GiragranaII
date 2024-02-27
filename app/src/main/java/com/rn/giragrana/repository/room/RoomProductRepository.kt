package com.rn.giragrana.repository.room
import androidx.lifecycle.LiveData
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository

class RoomProductRepository(
    database: GiragranaDatabase
) : ProductRepository {

    private val productDao = database.productDao()

    override fun save(product: Product) {
        if(product.id == 0L){
            val id = productDao.insert(product)
            product.id = id
        }else{
            productDao.update(product)
        }
    }

    override fun remove(vararg products: Product) {
        productDao.delete(*products)
    }

    override fun productById(id: Long): LiveData<Product> {
        return productDao.productById(id)
    }

    override fun search(term: String): LiveData<List<Product>> {
        return productDao.search(term)
    }
}