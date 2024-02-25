package com.rn.giragrana.repository.room

import androidx.lifecycle.LiveData
import com.rn.giragrana.model.Resale
import com.rn.giragrana.repository.sqlite.ResaleRepository

class RoomResaleRepository(
    database: GiragranaDatabase
) : ResaleRepository {

    private val resaleDao = database.resaleDao()
    override fun save(resale: Resale) {
        if(resale.id == 0L){
            val id = resaleDao.insert(resale)
            resale.id = id
        }else{
            resaleDao.update(resale)
        }
    }

    override fun remove(vararg resales: Resale) {
        resaleDao.delete(*resales)
    }

    override fun resaleById(id: Long): LiveData<Resale> {
        return resaleDao.resaleById(id)
    }

    override fun search(term: String): LiveData<List<Resale>> {
        return resaleDao.search(term)
    }
}