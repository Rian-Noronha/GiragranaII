package com.rnoronha.giragrana.di

import com.rnoronha.giragrana.auth.DeleteAccountViewModel
import com.rnoronha.giragrana.auth.DeleteAccountViewModelFactory
import com.rnoronha.giragrana.details.ProductDetailsViewModel
import com.rnoronha.giragrana.form.ClientFormViewModel
import com.rnoronha.giragrana.form.ProductFormViewModel
import com.rnoronha.giragrana.form.ResaleFormViewModel
import com.rnoronha.giragrana.list.ClientListViewModel
import com.rnoronha.giragrana.list.ProductListViewModel
import com.rnoronha.giragrana.list.ResaleListViewModel
import com.rnoronha.giragrana.repository.ClientRepository
import com.rnoronha.giragrana.repository.ProductRepository
import com.rnoronha.giragrana.repository.ResaleRepository
import com.rnoronha.giragrana.repository.room.GiragranaDatabase
import com.rnoronha.giragrana.repository.room.RoomClientRepository
import com.rnoronha.giragrana.repository.room.RoomProductRepository
import com.rnoronha.giragrana.repository.room.RoomResaleRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { GiragranaDatabase.getDatabase(context = get()) }
    single<ProductRepository> { RoomProductRepository(get()) }
    single<ClientRepository> { RoomClientRepository(get()) }
    single<ResaleRepository> { RoomResaleRepository(get()) }

    viewModel { ProductListViewModel(repository = get()) }
    viewModel { ProductFormViewModel(repository = get()) }
    viewModel { ProductDetailsViewModel(repository = get()) }
    viewModel { ClientListViewModel(repository = get()) }
    viewModel { ClientFormViewModel(repository = get()) }
    viewModel { ResaleListViewModel(repository = get()) }
    viewModel { ResaleFormViewModel(repository = get()) }
    factory { DeleteAccountViewModelFactory(get(), get()) }
    viewModel { DeleteAccountViewModelFactory(get(), get()).create(DeleteAccountViewModel::class.java) }
}
