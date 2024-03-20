package com.rn.giragrana.di

import com.rn.giragrana.details.ProductDetailsViewModel
import com.rn.giragrana.form.ClientFormViewModel
import com.rn.giragrana.form.ProductFormViewModel
import com.rn.giragrana.form.ResaleFormViewModel
import com.rn.giragrana.list.ClientListViewModel
import com.rn.giragrana.list.ProductListViewModel
import com.rn.giragrana.list.ResaleListViewModel
import com.rn.giragrana.repository.room.GiragranaDatabase
import com.rn.giragrana.repository.room.RoomClientRepository
import com.rn.giragrana.repository.room.RoomProductRepository
import com.rn.giragrana.repository.room.RoomResaleRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module{
    single{this}
    single{
        RoomProductRepository(GiragranaDatabase.getDatabase(context = get()))
    }

    single{
        RoomClientRepository(GiragranaDatabase.getDatabase(context = get()))
    }

    single{
        RoomResaleRepository(GiragranaDatabase.getDatabase(context = get()))
    }
    viewModel{
        ProductListViewModel(repository = get())
    }

    viewModel{
        ProductFormViewModel(repository = get())
    }

    viewModel{
        ProductDetailsViewModel(repository = get())
    }

    viewModel{
        ClientListViewModel(repository = get())
    }

    viewModel{
        ClientFormViewModel(repository = get())
    }

    viewModel{
        ResaleListViewModel(repository = get())
    }

    viewModel{
        ResaleFormViewModel(repository = get())
    }

}
