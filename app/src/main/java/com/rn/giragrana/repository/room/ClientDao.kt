package com.rn.giragrana.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rn.giragrana.model.Client
import com.rn.giragrana.repository.sqlite.CLIENT_COLUMN_ID
import com.rn.giragrana.repository.sqlite.CLIENT_COLUMN_NAME
import com.rn.giragrana.repository.sqlite.TABLE_CLIENT

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(client: Client): Long
    @Update
    fun update(client: Client): Int
    @Delete
    fun delete(vararg clients: Client): Int
    @Query("SELECT * FROM $TABLE_CLIENT WHERE $CLIENT_COLUMN_ID = :id")
    fun clientById(id: Long): LiveData<Client>
    @Query("""SELECT * FROM $TABLE_CLIENT WHERE $CLIENT_COLUMN_NAME LIKE :query ORDER BY $CLIENT_COLUMN_NAME""")
    fun search(query: String) : LiveData<List<Client>>

    @Query("""SELECT * FROM $TABLE_CLIENT""")
    fun getAllClients(): LiveData<List<Client>>

    @Query("""DELETE FROM $TABLE_CLIENT""")
    suspend fun deleteAllClients()
}