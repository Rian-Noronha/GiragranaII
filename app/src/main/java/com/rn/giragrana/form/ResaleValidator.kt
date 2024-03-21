package com.rn.giragrana.form

import com.rn.giragrana.model.Resale
class ResaleValidator {
    fun validate(info: Resale) = with(info){
        checkPaymentMethod(paymentMethod)
    }

    private fun checkPaymentMethod(paymentMethod: String) = paymentMethod.length in 2..30
}