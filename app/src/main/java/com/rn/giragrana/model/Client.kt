package com.rn.giragrana.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rn.giragrana.repository.sqlite.CLIENT_COLUMN_ID
import com.rn.giragrana.repository.sqlite.TABLE_CLIENT

@Entity(tableName = TABLE_CLIENT)
data class Client(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CLIENT_COLUMN_ID)
    var id:Long = 0,
    var name:String = "",
    var contact:String = "",
){
    override fun toString(): String = name
}
