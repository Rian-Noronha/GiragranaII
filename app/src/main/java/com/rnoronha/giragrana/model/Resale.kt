package com.rnoronha.giragrana.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rnoronha.giragrana.repository.sqlite.CLIENT_COLUMN_ID
import com.rnoronha.giragrana.repository.sqlite.PRODUCT_COLUMN_ID
import com.rnoronha.giragrana.repository.sqlite.RESALE_COLUMN_CLIENT_ID
import com.rnoronha.giragrana.repository.sqlite.RESALE_COLUMN_ID
import com.rnoronha.giragrana.repository.sqlite.RESALE_COLUMN_PRODUCT_ID
import com.rnoronha.giragrana.repository.sqlite.TABLE_RESALE

@Entity(tableName = TABLE_RESALE,
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = [PRODUCT_COLUMN_ID],
            childColumns = [RESALE_COLUMN_PRODUCT_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Client::class,
            parentColumns = [CLIENT_COLUMN_ID],
            childColumns = [RESALE_COLUMN_CLIENT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Resale(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RESALE_COLUMN_ID)
    var id:Long = 0,
    @ColumnInfo(name = RESALE_COLUMN_PRODUCT_ID)
    var productId: Long = 0,
    @ColumnInfo(name = RESALE_COLUMN_CLIENT_ID)
    var clientId: Long = 0,
    var resalePrice: Float = 0.0F,
    var date:String = "",
    var receivingDate:String = "",
    var paymentMethod:String = "",
)
