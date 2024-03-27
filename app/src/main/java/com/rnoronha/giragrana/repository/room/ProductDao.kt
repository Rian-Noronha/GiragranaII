package com.rnoronha.giragrana.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rnoronha.giragrana.model.Product
import com.rnoronha.giragrana.repository.sqlite.PRODUCT_COLUMN_ID
import com.rnoronha.giragrana.repository.sqlite.PRODUCT_COLUMN_NAME
import com.rnoronha.giragrana.repository.sqlite.PRODUCT_COLUMN_SOLD
import com.rnoronha.giragrana.repository.sqlite.TABLE_PRODUCT
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product): Long
    @Update
    fun update(product: Product): Int
    @Delete
    fun delete(vararg products: Product): Int
    @Query("SELECT * FROM $TABLE_PRODUCT WHERE $PRODUCT_COLUMN_ID = :id")
    fun productById(id: Long): LiveData<Product>

    @Query("""SELECT * FROM $TABLE_PRODUCT WHERE $PRODUCT_COLUMN_NAME LIKE :query ORDER BY $PRODUCT_COLUMN_ID""")
    fun search(query: String): LiveData<List<Product>>

    @Query("""SELECT * FROM $TABLE_PRODUCT WHERE NOT $PRODUCT_COLUMN_SOLD ORDER BY $PRODUCT_COLUMN_ID""")
    fun unsoldProducts(): LiveData<List<Product>>

    @Query("""SELECT * FROM $TABLE_PRODUCT""")
    fun getAllProducts(): LiveData<List<Product>>
    @Query("""DELETE FROM $TABLE_PRODUCT""")
    suspend fun deleteAllProducts()

}