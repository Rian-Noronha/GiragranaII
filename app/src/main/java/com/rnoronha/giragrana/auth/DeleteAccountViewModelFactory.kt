package com.rnoronha.giragrana.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rnoronha.giragrana.repository.ClientRepository
import com.rnoronha.giragrana.repository.ProductRepository

class DeleteAccountViewModelFactory(
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteAccountViewModel::class.java)) {
            return DeleteAccountViewModel(clientRepository, productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}