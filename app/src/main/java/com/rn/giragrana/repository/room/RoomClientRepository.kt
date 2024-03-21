package com.rn.giragrana.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.rn.giragrana.model.Client
import com.rn.giragrana.repository.ClientRepository

class RoomClientRepository(
    database: GiragranaDatabase
) : ClientRepository {

    private val clientDao = database.clientDao()
    override fun save(client: Client) {
        if(client.id == 0L){
            val id = clientDao.insert(client)
            client.id = id
        }else{
            clientDao.update(client)
        }
    }

    override fun remove(vararg clients: Client) {
        clientDao.delete(*clients)
    }

    override fun clientById(id: Long): LiveData<Client> {
        return clientDao.clientById(id)
    }

    override fun search(term: String): LiveData<List<Client>>{
        return clientDao.search(term)
    }

    override fun getClientsMap(): LiveData<Map<Long, Client>> {
        val result = MediatorLiveData<Map<Long, Client>>()
        val allClientsLiveData = clientDao.getAllClients()

        result.addSource(allClientsLiveData) { clients ->
            result.value = clients.associateBy { it.id }
        }

        return result
    }

    override suspend fun deleteAllClients() {
       clientDao.deleteAllClients()
    }


}