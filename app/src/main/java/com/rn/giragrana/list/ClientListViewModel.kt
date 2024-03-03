package com.rn.giragrana.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.rn.giragrana.common.SingleLiveEvent
import com.rn.giragrana.model.Client
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ClientRepository

class ClientListViewModel(
    private val repository: ClientRepository
) : ViewModel(){
    var idClientSelected: Long = -1
    private val searchTerm = MutableLiveData<String>()
    private val clients: LiveData<List<Client>> = searchTerm.switchMap { term ->
        repository.search("%$term")
    }
    private val inDeleteMode = MutableLiveData<Boolean>().apply{
        value = false
    }
    private val selectedItems = mutableListOf<Client>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedClients = MutableLiveData<List<Client>>().apply{
        value = selectedItems
    }
    private val deletedItems = mutableListOf<Client>()
    private val showDeleteMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Client>()


    private val _clientsMap = MutableLiveData<Map<Long, Client>>()
    val clientsMap: LiveData<Map<Long, Client>> get() = _clientsMap

    init {
        repository.getClientsMap().observeForever { map ->
            _clientsMap.value = map
        }
    }


    fun isInDeleteMode(): LiveData<Boolean> = inDeleteMode
    fun getSearchTerm(): LiveData<String>? = searchTerm
    fun getClients(): LiveData<List<Client>>? = clients
    fun selectionCount(): LiveData<Int> = selectionCount
    fun selectedClients(): LiveData<List<Client>> = selectedClients
    fun showDeletedMessage(): LiveData<Int> = showDeleteMessage
    fun showDetailsCommand(): LiveData<Client> = showDetailsCommand

    fun selectClient(client: Client){
        if(inDeleteMode.value == true){
            toggleClientSelected(client)
            if(selectedItems.size == 0){
                inDeleteMode.value = false
            }else{
                selectionCount.value = selectedItems.size
                selectedClients.value = selectedItems
            }
        }else{
            showDetailsCommand.value = client
        }
    }

    private fun toggleClientSelected(client: Client){
        val existing = selectedItems.find{it.id == client.id}
        if(existing == null){
            selectedItems.add(client)
        }else{
            selectedItems.removeAll{it.id == client.id}
        }
    }

    fun search(term: String=""){
        searchTerm.value = term
    }

    fun setInDeleteMode(deleteMode: Boolean){
        if(!deleteMode){
            selectionCount.value = 0
            selectedItems.clear()
            selectedClients.value = selectedItems
            showDeleteMessage.value = selectedItems.size
        }

        inDeleteMode.value = deleteMode
    }

    fun deleteSelected(){
        repository.remove(*selectedItems.toTypedArray())
        deletedItems.clear()
        deletedItems.addAll(selectedItems)
        setInDeleteMode(false)
        showDeleteMessage.value = deletedItems.size
    }

    fun undoDelete(){
        if(deletedItems.isNotEmpty()){
            for(client in deletedItems){
                client.id = 0L
                repository.save(client)
            }
        }
    }

}