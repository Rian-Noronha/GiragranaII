package com.rn.giragrana.di
import com.rn.giragrana.form.ProductFormViewModel
import com.rn.giragrana.list.ProductListViewModel
import com.rn.giragrana.repository.ProductRepository
import com.rn.giragrana.repository.room.GiragranaDatabase
import com.rn.giragrana.repository.room.RoomProductRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module{
    single{this}
    single{
        RoomProductRepository(GiragranaDatabase.getDatabase(context = get())) as ProductRepository
    }

    viewModel{
        ProductListViewModel(repository = get())
    }

    viewModel{
        ProductFormViewModel(repository = get())
    }
}
