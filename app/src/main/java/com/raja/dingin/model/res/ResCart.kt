package com.raja.dingin.model.res

data class ResCart(
    val harga: Int,
    val id: Int,
    val inv_id: Int,
    val name_product: String,
    val sub_total: Int,
    val qty: Int,
    val sisa_stok: Int,
    val stok_awal: Int,
    val image: String
)