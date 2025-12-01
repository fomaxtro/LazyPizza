package com.fomaxtro.core.presentation.ui

import android.icu.math.BigDecimal
import android.icu.text.NumberFormat
import android.icu.util.Currency
import java.time.format.DateTimeFormatter

object Formatters {
    val pickupTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, HH:mm")

    fun formatCurrency(price: Double): String {
        return NumberFormat
            .getCurrencyInstance()
            .apply {
                currency = Currency.getInstance("USD")
                maximumFractionDigits = 2
                roundingMode = BigDecimal.ROUND_FLOOR
            }
            .format(price)
    }

}