package com.rn.giragrana.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rn.giragrana.model.Client
import com.rn.giragrana.repository.ClientRepository

class ClientFormViewModel(
    private val repository: ClientRepository
) : ViewModel(){
    private val validator by lazy{ClientValidator()}

    fun loadClient(id: Long): LiveData<Client>{
        return repository.clientById(id)
    }

    fun saveClient(client: Client): Boolean{
        return validator.validate(client)
            .also { validated ->
                if(validated) repository.save(client)
            }
    }
}