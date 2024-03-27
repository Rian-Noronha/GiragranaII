package com.rnoronha.giragrana.form

import com.rnoronha.giragrana.model.Resale
class ResaleValidator {
    fun validate(info: Resale) = with(info){
        checkPaymentMethod(paymentMethod)
    }

    private fun checkPaymentMethod(paymentMethod: String) = paymentMethod.length in 2..30
}