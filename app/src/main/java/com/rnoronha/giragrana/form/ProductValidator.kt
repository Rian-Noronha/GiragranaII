package com.rnoronha.giragrana.form

import com.rnoronha.giragrana.model.Product

class ProductValidator {
    fun validate(info: Product) = with(info){
        checkName(name) && checkDescription(description)
    }

    private fun checkName(name: String) = name.length in 2..40
    private fun checkDescription(description: String) = description.length in 4..200
}