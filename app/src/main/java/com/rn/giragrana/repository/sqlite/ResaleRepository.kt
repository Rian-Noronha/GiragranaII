package com.rn.giragrana.repository.sqlite

import androidx.lifecycle.LiveData
import com.rn.giragrana.model.Resale

interface ResaleRepository {
    fun save(resale: Resale)
    fun remove(vararg resales: Resale)
    fun resaleById(id:Long): LiveData<Resale>
    fun search(term: String): LiveData<List<Resale>>
}