package com.rn.giragrana.utils

import java.text.NumberFormat
import java.util.Locale

object PriceUtils {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun formatPrice(amount: Float): String {
        return currencyFormat.format(amount)
    }


}