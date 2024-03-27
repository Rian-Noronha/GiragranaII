package com.rnoronha.giragrana.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rnoronha.giragrana.model.Resale
import com.rnoronha.giragrana.repository.ResaleRepository

class ResaleFormViewModel(
    private val repository: ResaleRepository
) : ViewModel(){
    private val validator by lazy{ResaleValidator()}
    fun loadResale(id: Long): LiveData<Resale>{
        return repository.resaleById(id)
    }

    fun saveResale(resale: Resale): Boolean{
        return validator.validate(resale)
            .also{validated ->
                if(validated) repository.save(resale)
            }
    }
}