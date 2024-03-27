package com.rnoronha.giragrana.repository

import androidx.lifecycle.LiveData
import com.rnoronha.giragrana.model.Resale

interface ResaleRepository {
    fun save(resale: Resale)
    fun remove(vararg resales: Resale)
    fun resaleById(id:Long): LiveData<Resale>
    fun search(term: String): LiveData<List<Resale>>
}