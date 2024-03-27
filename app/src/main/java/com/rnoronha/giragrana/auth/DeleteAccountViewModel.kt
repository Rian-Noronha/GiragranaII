package com.rnoronha.giragrana.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rnoronha.giragrana.repository.ClientRepository
import com.rnoronha.giragrana.repository.ProductRepository
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    fun deleteAllClientsAndProducts() {
        viewModelScope.launch {
            deleteAllProducts()
            deleteAllClients()

        }
    }

    private suspend fun deleteAllClients() {
        clientRepository.deleteAllClients()
    }

    private suspend fun deleteAllProducts() {
        productRepository.deleteAllProducts()
    }

}