package com.rn.giragrana.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rn.giragrana.model.Resale
import com.rn.giragrana.repository.sqlite.RESALE_COLUMN_ID
import com.rn.giragrana.repository.sqlite.RESALE_COLUMN_PAYMENT_METHOD
import com.rn.giragrana.repository.sqlite.TABLE_RESALE
@Dao
interface ResaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(resale: Resale): Long
    @Update
    fun update(resale: Resale): Int
    @Delete
    fun delete(vararg resales: Resale): Int
    @Query("SELECT * FROM $TABLE_RESALE WHERE $RESALE_COLUMN_ID = :id")
    fun resaleById(id: Long): LiveData<Resale>

    @Query("""SELECT * FROM $TABLE_RESALE WHERE $RESALE_COLUMN_ID LIKE :query ORDER BY $RESALE_COLUMN_PAYMENT_METHOD""")
    fun search(query: String): LiveData<List<Resale>>
}