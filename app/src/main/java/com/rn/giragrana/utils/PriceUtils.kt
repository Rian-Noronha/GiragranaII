package com.rn.giragrana.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
object PriceUtils {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun formatPrice(amount: Float): String {
        return currencyFormat.format(amount)
    }


}