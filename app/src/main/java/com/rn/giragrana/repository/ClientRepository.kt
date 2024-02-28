package com.rn.giragrana.repository

import androidx.lifecycle.LiveData
import com.rn.giragrana.model.Client

interface ClientRepository {
    fun save(client: Client)
    fun remove(vararg clients: Client)
    fun clientById(id:Long): LiveData<Client>
    fun search(term: String): LiveData<List<Client>>
}