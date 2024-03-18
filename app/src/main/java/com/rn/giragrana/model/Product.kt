package com.rn.giragrana.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rn.giragrana.repository.sqlite.PRODUCT_COLUMN_ID
import com.rn.giragrana.repository.sqlite.TABLE_PRODUCT

@Entity(tableName = TABLE_PRODUCT)
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PRODUCT_COLUMN_ID)
    var id:Long = 0,
    var name:String = "",
    var description:String = "",
    var price: Float = 0.0F,
    var sold: Boolean = false
){
    override fun toString(): String = name
}
