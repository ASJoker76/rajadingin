package com.raja.dingin.utils

import java.util.*

class ToRupiah {
    fun setConvertRp(harga: Int): String {
        var format = String.format(Locale.getDefault(), "%,d", harga)
        format = format.replace(",", ".")
        return "Rp $format"
    }
}